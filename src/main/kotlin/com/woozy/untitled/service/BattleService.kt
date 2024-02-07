package com.woozy.untitled.service

import com.woozy.untitled.dto.BattleDto
import com.woozy.untitled.dto.request.HuntRequestDto
import com.woozy.untitled.dto.response.HuntingResponseDto
import com.woozy.untitled.exception.CustomException
import com.woozy.untitled.exception.ErrorCode
import com.woozy.untitled.infra.rabbit.MessagePublisher
import com.woozy.untitled.dto.BattleInfo
import com.woozy.untitled.infra.redis.RedisService
import com.woozy.untitled.infra.security.UserPrincipal
import com.woozy.untitled.model.*
import com.woozy.untitled.model.enums.ItemCategory
import com.woozy.untitled.model.enums.MonsterType
import com.woozy.untitled.repository.MonsterRepository
import com.woozy.untitled.repository.PlayerItemRepository
import com.woozy.untitled.repository.PlayerRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BattleService(
    private val playerRepository: PlayerRepository,
    private val monsterRepository: MonsterRepository,
    private val messagePublisher: MessagePublisher,
    private val redisService: RedisService,
    private val playerItemRepository: PlayerItemRepository

) {
    @Transactional
    //TODO: 드랍템 리턴 해야겠지?
    fun applyBattleResult(battle: BattleInfo) {
        val player = playerRepository.findByIdOrNull(battle.battleDto.playerId)
            ?: throw CustomException(ErrorCode.PLAYER_NOT_FOUND)

        val monster = monsterRepository.findByIdFetchDropTable(battle.battleDto.monsterId)
            ?: throw CustomException(ErrorCode.MONSTER_NOT_FOUND)

        val itemDropTable = monster.itemDropTable
        val goodsDropTable = monster.goodsDropTable

        player.earnRewards(battle.battleDto)
        player.goNextStageIfBoss(battle.battleDto)
        val playerItem = createItemInstance(player, itemDropTable)
        playerItemRepository.saveAll(playerItem)
        addPlayerGoods(goodsDropTable, player)

    }


    @Transactional
    fun addPlayerGoods(goodsDropTable: GoodsDropTable, player: Player){
        val playerGoodsList = player.playerGoodsList
        if (goodsDropTable.isDropped()) {
            playerGoodsList.map {
                if (it.goods.category == goodsDropTable.goods.category){
                    it.increase(goodsDropTable.amount)
                }
            }
        }
        playerRepository.save(player)
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
                type = it.type,
                category = it.category,
                remainingCnt = it.category.upLimit,
            )
        }.toList()
    }


    @Transactional
    fun hunt(huntRequestDto: HuntRequestDto, playerId: Long, userPrincipal: UserPrincipal): HuntingResponseDto {

        ServiceUtil.checkPlayerId(playerId, userPrincipal)

        val player = playerRepository.findByIdOrNull(playerId)
            ?: throw CustomException(ErrorCode.PLAYER_NOT_FOUND)

        player.checkStageAvailable(huntRequestDto.selectedStage)
        redisService.checkBattleInProgress(playerId)

        val monsterType = MonsterType.randomType(huntRequestDto.isBoss)
        val monster = monsterRepository.findByStageAndType(huntRequestDto.selectedStage, monsterType)
            ?: throw CustomException(ErrorCode.MONSTER_NOT_FOUND)

        val battleResult = battle(player, monster)

        if (battleResult.hasPlayerWon) {
            val battleInfo = BattleInfo(battleResult)
            redisService.saveBattleInfo(battleInfo)
            messagePublisher.sendDelayedMessage(battleResult.delayTimeMilliSeconds, battleInfo.battleId)
        }

        return HuntingResponseDto(
            hasPlayerWon = battleResult.hasPlayerWon,
            expectedTimeSeconds = battleResult.delayTimeMilliSeconds / 1000,
            encounteredMonster = monster.name,
            expectedGold = monster.goldReward,
            expectedExp = monster.expReward
        )

    }

    fun battle(player: Player, monster: Monster): BattleDto{
        val firstAttacker = if (player.level >= monster.level) Combatant(player) else Combatant(monster)
        val secondAttacker = if (firstAttacker.isPlayer) Combatant(monster) else Combatant(player)
        val winner: Combatant
        val battleLog = StringBuffer()
        var limitCnt = 0
        val expectedTimeSeconds: Double = timeToKill(player, monster)

        battleLog.appendLine("${monster.name} 이/(가) 나타났다!!")
        battleLog.appendLine("${firstAttacker.name} 의 선공!")

        while(true){
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

            if(limitCnt++ > 20) {
                winner = Combatant(monster)
                battleLog.appendLine("전투 시간 초과!!")
                break
            }
        }
        return BattleDto(
            playerId = player.id!!,
            monsterId = monster.id!!,
            stage = monster.stage,
            monsterType = monster.type,
            hasPlayerWon = winner.isPlayer,
            battleLog = battleLog.toString(),
            delayTimeMilliSeconds = (expectedTimeSeconds * 1000).toLong(),
            expectedGold = monster.goldReward,
            expectedExp = monster.expReward
        )
    }

    fun timeToKill(player: Player, monster: Monster): Double {
        //TODO: 수정해야한다
        val playerTotalAd = (player.atkDmg + player.addiAtkDmg).toDouble()
        val secondsPerAtk: Double = 60 / (player.atkSpd + player.addiAtkSpd).toDouble()
        val monsterHp = monster.hitPnt.toDouble()

        val timeToKillMonster = monsterHp / playerTotalAd * secondsPerAtk
        println("$monsterHp / $secondsPerAtk * $playerTotalAd")

        return timeToKillMonster
    }
}