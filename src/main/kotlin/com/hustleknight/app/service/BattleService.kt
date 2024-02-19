package com.hustleknight.app.service

import com.hustleknight.app.dto.BattleDto
import com.hustleknight.app.dto.BattleInfo
import com.hustleknight.app.dto.request.HuntRequestDto
import com.hustleknight.app.dto.response.DropResponseDto
import com.hustleknight.app.dto.response.HuntingResponseDto
import com.hustleknight.app.dto.response.RaidResponseDto
import com.hustleknight.app.exception.CustomException
import com.hustleknight.app.exception.ErrorCode
import com.hustleknight.app.infra.rabbit.MessagePublisher
import com.hustleknight.app.infra.redis.RedisService
import com.hustleknight.app.model.*
import com.hustleknight.app.model.enums.ItemCategory
import com.hustleknight.app.model.enums.MaxValues
import com.hustleknight.app.model.enums.MonsterType
import com.hustleknight.app.repository.MonsterRepository
import com.hustleknight.app.repository.PlayerItemRepository
import com.hustleknight.app.repository.PlayerRepository
import com.hustleknight.app.repository.RaidMonsterRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BattleService(
    private val playerRepository: PlayerRepository,
    private val monsterRepository: MonsterRepository,
    private val playerItemRepository: PlayerItemRepository,
    private val raidMonsterRepository: RaidMonsterRepository,
    private val messagePublisher: MessagePublisher,
    private val redisService: RedisService
) {

    fun abort(playerId: Long, idFromToken: Long): String {
        ServiceUtil.checkAuth(playerId, idFromToken)
        redisService.stopBattle(playerId)
        return "success"
    }

    @Transactional
    fun hunt(huntRequestDto: HuntRequestDto, playerId: Long, idFromToken: Long): HuntingResponseDto {

        ServiceUtil.checkAuth(playerId, idFromToken)

        //TODO: 프론트에서 사냥하기 요청 전 연결확인을통해 . .
        redisService.checkEmitterExistence(playerId)

        val player = playerRepository.findByIdOrNull(playerId)
            ?: throw CustomException(ErrorCode.PLAYER_NOT_FOUND)

        player.checkStageAvailable(huntRequestDto.selectedStage)

        //TODO: transaction? & try - catch?
        redisService.checkBattleInProgress(playerId)

        val monsterType = MonsterType.randomType(huntRequestDto.isBoss)
        val monster = monsterRepository.findByStageAndType(huntRequestDto.selectedStage, monsterType)
            ?: throw CustomException(ErrorCode.MONSTER_NOT_FOUND)

        val battleResult = battle(player, monster)
        val expectedDrop = DropResponseDto.fromEntityToExpect(monster)

        if (battleResult.hasPlayerWon) {
            val battleInfo = BattleInfo(battleResult, expectedDrop)
            redisService.saveBattleInfo(battleInfo)
            messagePublisher.sendDelayedMessage(battleResult.delayTimeMilliSeconds, battleInfo.battleId)
        }


        return HuntingResponseDto(
            hasPlayerWon = battleResult.hasPlayerWon,
            expectedTimeSeconds = battleResult.delayTimeMilliSeconds / 1000,
            encounteredMonster = monster.name,
            expectedGold = monster.goldReward,
            expectedExp = monster.expReward,
            expectedDrop = expectedDrop
        )

    }

    fun battle(player: Player, monster: Monster): BattleDto{
        val firstAttacker = if (player.level >= monster.level) Combatant(player) else Combatant(monster)
        val secondAttacker = if (firstAttacker.isPlayer) Combatant(monster) else Combatant(player)
        val winner: Combatant
        val battleLog = StringBuffer()
        var turn = 0
        val expectedTimeSeconds: Double = expectedBattleTime(player, monster)
        var expectedGold = 0L
        var expectedExp = 0L


        battleLog.appendLine("${monster.name} 이/(가) 나타났다!!")
        battleLog.appendLine("${firstAttacker.name} 의 선공!")

        while(true){

            if(++turn > MaxValues.MAX_TURN.value) {
                turn -= 1
                winner = Combatant(monster)
                battleLog.appendLine("전투 시간 초과!!")
                break
            }

            firstAttacker.attack(secondAttacker, battleLog)
            if (firstAttacker.isEnemyDied(secondAttacker, battleLog)){
                winner = firstAttacker
                break
            }
            secondAttacker.attack(firstAttacker, battleLog)
            if (secondAttacker.isEnemyDied(firstAttacker, battleLog)){
                winner = secondAttacker
                break
            }


        }
        if(winner.isPlayer) {
            expectedExp = monster.expReward
            expectedGold = monster.goldReward
        }
        return BattleDto(
            playerId = player.id!!,
            monsterId = monster.id!!,
            stage = monster.stage,
            monsterType = monster.type,
            hasPlayerWon = winner.isPlayer,
            turn = turn,
            battleLog = battleLog.toString(),
            delayTimeMilliSeconds = (expectedTimeSeconds * 1000).toLong(),
            expectedGold = expectedGold,
            expectedExp = expectedExp
        )
    }

    @Transactional
    fun applyBattleResult(battle: BattleInfo): DropResponseDto {
        val player = playerRepository.findByIdOrNull(battle.battleDto.playerId)
            ?: throw CustomException(ErrorCode.PLAYER_NOT_FOUND)

        val monster = monsterRepository.findByIdFetchDropTable(battle.battleDto.monsterId)
            ?: throw CustomException(ErrorCode.MONSTER_NOT_FOUND)

        val itemDropTable = monster.itemDropTable
        val goodsDropTable = monster.goodsDropTable

        player.earnRewards(monster)
        player.goNextStageIfBoss(monster)
        val playerItemList = createItemInstance(player, itemDropTable)
        playerItemRepository.saveAll(playerItemList)
        player.earnGoods(goodsDropTable)
        playerRepository.save(player)

        return DropResponseDto.fromEntityToDropped(playerItemList, goodsDropTable)
    }

    fun addPlayerGoods(goodsDropTable: GoodsDropTable, player: Player){
        val playerGoodsList = player.playerGoodsList
        if (goodsDropTable.isDropped()) {
            playerGoodsList.map {
                if (it.goods.category == goodsDropTable.goods.category){
                    it.increase(goodsDropTable.amount)
                }
            }
        }
    }

    private fun createItemInstance(player: Player, itemDropTable: List<ItemDropTable>): List<PlayerItem> {
        val droppedItemList = itemDropTable.mapNotNull {
            if (ItemCategory.isDropped(it.dropRate)) {
                it.item
            } else {
                null
            }
        }

        return droppedItemList.map {
            PlayerItem(
                player = player,
                name = it.name,
                reqLevel = it.reqLevel,
                attrName = it.attrName,
                finalAttrValue = ItemCategory.attrWithOption(it.attrValue),
                category = it.category,
                remainingCnt = it.category.upLimit,
            )
        }.toList()
    }

    private fun expectedBattleTime(player: Player, monster: Monster): Double {
        //TODO: 수정해야한다 만약 저기면 service util 에
        //TODO: player 가 먼저 죽는 경우도 생각해야하나?
        val playerTotalAd = (player.atkDmg + player.addiAtkDmg).toDouble()
        val secondsPerAtk: Double = 60 / (player.atkSpd + player.addiAtkSpd).toDouble()
        val monsterHp = monster.hitPnt.toDouble()
        val timeToClear = monsterHp / playerTotalAd * secondsPerAtk

        return timeToClear
    }

    @Transactional
    fun raid(playerId: Long, idFromToken: Long, raidMonsterId: Long): RaidResponseDto {
        ServiceUtil.checkAuth(playerId, idFromToken)

        val player = playerRepository.findByIdOrNull(playerId)
            ?: throw CustomException(ErrorCode.PLAYER_NOT_FOUND)

        player.consumeTicket()

        val raidMonster = raidMonsterRepository.findWithLockById(raidMonsterId)
//        val raidMonster = raidMonsterRepository.findByIdOrNull(raidMonsterId)
            ?: throw CustomException(ErrorCode.MONSTER_NOT_FOUND)

        val score = raidMonster.underAttack(player)
        player.earnScore(score)

        return RaidResponseDto(score, raidMonster.totalHp)
    }
}