//package com.woozy.untitled.service
//
//import com.woozy.untitled.dto.BattleResult
//import com.woozy.untitled.dto.request.HuntRequestDto
//import com.woozy.untitled.dto.response.HuntResponseDto
//import com.woozy.untitled.dto.response.MonsterResponseDto
//import com.woozy.untitled.exception.CustomException
//import com.woozy.untitled.exception.ErrorCode
//import com.woozy.untitled.infra.security.UserPrincipal
//import com.woozy.untitled.model.*
//import com.woozy.untitled.model.enums.MonsterType
//import com.woozy.untitled.model.enums.RandomRate
//import com.woozy.untitled.repository.PlayerGoodsRepository
//import com.woozy.untitled.repository.PlayerItemRepository
//import com.woozy.untitled.repository.MonsterRepository
//import com.woozy.untitled.repository.PlayerRepository
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.delay
//import kotlinx.coroutines.withContext
//import org.springframework.data.repository.findByIdOrNull
//import org.springframework.stereotype.Service
//import org.springframework.transaction.annotation.Transactional
//import kotlin.random.Random
//
//@Service
//class OldBattleService(
//    private val playerRepository: PlayerRepository,
//    private val monsterRepository: MonsterRepository,
//    private val playerItemRepository: PlayerItemRepository,
//    private val playerGoodsRepository: PlayerGoodsRepository
//) {
//    suspend fun hunt(huntRequestDto: HuntRequestDto, playerId: Long, idFromToken: Long): HuntResponseDto {
////        ServiceUtil.checkPlayerId(playerId, userPrincipal.id)
//        val player = playerRepository.findByIdOrNull(playerId)
//            ?: throw CustomException(ErrorCode.PLAYER_NOT_FOUND)
//
//        player.checkStageAvailable(huntRequestDto.selectedStage)
//
//        //TODO: 이것도 MONSTER TYPE ENUM 에서 METHOD 화
//        val monster = monsterRepository.findByStageAndType(
//            huntRequestDto.selectedStage,
//            randomTypeByRate(huntRequestDto.isBoss, RandomRate.MUTANT_RATE.value)
//        )
//            ?: throw CustomException(ErrorCode.MONSTER_NOT_FOUND)
//
//        println(monster.name + "!!!!!!!!!!!!!!")
//        // 배틀이랑 같은 tx에 있으면 안될 듯
//        val battleResult: BattleResult
//        withContext(Dispatchers.IO) {
//            battleResult = battle(player, monster)
//            applyBattleResult(battleResult, player, monster)
//        }
//
//        return HuntResponseDto(
//            hasWon = battleResult.isPlayerWon,
//            earnedExp = battleResult.droppedExp,
//            earnedGold = battleResult.droppedGold,
//            isLevelUp = battleResult.isLevelUp,
//            droppedItemList = battleResult.droppedItemList,
//            droppedGoodsList = battleResult.droppedGoods,
//            battleLog = battleResult.battleLog.toString(),
//            expInfo = battleResult.expInfo,
//            monster = MonsterResponseDto.fromEntity(monster)
//        )
//
//    }
//
//    // 트랜잭션을 물고 있는 시간을 최소화하기 위해 battle 이후 데이터를 저장할 때만 @Transactional 활성화
//    @Transactional
//    fun applyBattleResult(
//        battleResult: BattleResult,
//        player: Player,
//        monster: Monster
//    ) {
//        if (battleResult.isPlayerWon) {
//            val levelBefore = player.level
//            // 골드와 경험치 획득
//            player.earnRewards(monster)
//            //TODO: BattleResult 의 method로 빼서 Service Logic 을 간결하게 만들자.
//            battleResult.isLevelUp = (player.level != levelBefore)
//            battleResult.expInfo = "${player.exp} / ${player.level * player.level * 50}"
//            battleResult.droppedExp = monster.expReward
//            battleResult.droppedGold = monster.goldReward
//            //강화석 획득
//
//            //스테이지 확인 후 넘어가게 해주기?
//            player.goNextStageIfBoss(monster)
//            // 드랍테이블 ㄱ
//            // monster <-> dropTable 연관관계 설정
////            val dropTable = dropTableRepository.findByMonsterId(monster.id!!)
//            val droppedItemList = getDroppedItemList(monster.itemDropTable)
//            //createItemInstance
//            //TODO: 이름 바꾸기
//            val equipmentList = createItemInstance(player, droppedItemList)
//            battleResult.addAllPlayerItem(equipmentList)
//
//            //재화 추가
//            val goodsDropTable = monster.goodsDropTable
//            if (goodsDropTable.isDropped()) {
//                addPlayerGoods(player, goodsDropTable)
//            }
//
//            playerRepository.save(player)
//            playerItemRepository.saveAll(equipmentList)
//
//        }
//    }
//
//    @Transactional
//    fun addPlayerGoods(player: Player, goodsDropTable: GoodsDropTable) {
//        val currencyList = playerGoodsRepository.findPlayerGoodsByPlayerId(player.id!!)
//        //TODO: mapNotNull 쓰면 되지않나
//        currencyList.forEach {
//            if (it.goods.category == goodsDropTable.goods.category){
//                it.amount += goodsDropTable.amount
//            }
//        }
//        playerGoodsRepository.saveAll(currencyList)
//    }
//
//    private fun createItemInstance(player: Player, droppedItemList: List<Item>): List<PlayerItem> {
//        return droppedItemList.map {
//            PlayerItem(
//                player = player,
//                name = it.name,
//                reqLevel = it.reqLevel,
//                attrName = it.attrName,
//                finalAttrValue = attrWithOption(it.attrValue),
//                type = it.type,
//                category = it.category,
//                remainingCnt = it.category.upLimit,
//            )
//        }.toList()
//    }
//
//
//    private fun attrWithOption(attrValue: Double): Double {
//        val result = attrValue + (Random.nextInt(11) - 5)
//        if (result <= 0) {
//            throw Exception()
//        }
//        return result
//    }
//
//    private fun getDroppedItemList(itemDropTable: List<ItemDropTable>): List<Item> {
//        return itemDropTable.mapNotNull {
//            if (Random.nextDouble() < it.dropRate) {
//                it.item
//            } else {
//                null
//            }
//        }.toList()
//    }
//
//    private fun isGoodsDropped(goodsDropTable: GoodsDropTable) : Boolean {
//        return Random.nextDouble() < goodsDropTable.dropRate
//    }
//
//    //TODO: enum 으로 옮겨?
//    private fun randomTypeByRate(isBoss: Boolean, rate: Double): MonsterType {
//        if (isBoss) {
//            return MonsterType.BOSS
//        }
//        val randomDouble = Random.nextDouble()
//        val result =
//            when {
//                randomDouble < rate -> {
//                    MonsterType.MUTANT
//                }
//
//                else -> {
//                    MonsterType.NORMAL
//                }
//            }
//
//        return result
//    }
//
//    private suspend fun battle(player: Player, monster: Monster): BattleResult {
//        //레벨이 높은쪽이 선공?
//        //체력 - 공격력 했을 때 0보다 작거나 같으면 뒤짐
//        //초당 공격속도 이므로 몇번 반복해야하는지 횟수가 초가되겠네
//        val playerAtkSpd = player.atkSpd + player.addiAtkSpd
//        val firstAttacker = if (player.level >= monster.level) Combatant(player) else Combatant(monster)
//        val secondAttacker = if (firstAttacker.isPlayer) Combatant(monster) else Combatant(player)
//        val winner: Combatant
//        val battleLog = StringBuffer()
//        var limitCnt = 0
//
//        println("전투시작!!")
//
//        while (true) {
//
//            delay((playerAtkSpd * 1000).toLong())
//
//            firstAttacker.attack(secondAttacker)
//            appendBattleLog(battleLog, firstAttacker, secondAttacker)
//            if (secondAttacker.hitPnt <= 0) {
//                appendDieLog(battleLog, secondAttacker)
//                winner = firstAttacker
//                break
//            }
//
//            secondAttacker.attack(firstAttacker)
//            appendBattleLog(battleLog, secondAttacker, firstAttacker)
//            if (firstAttacker.hitPnt <= 0) {
//                appendDieLog(battleLog, firstAttacker)
//                winner = secondAttacker
//                break
//            }
//            println("전투중!!")
//
//            if (limitCnt++ > 20) {
//                winner = Combatant(monster)
//                break
//            }
//
//        }
//
//        return BattleResult(
//            isBoss(monster),
//            winner.isPlayer,
//            monster.stage,
//            battleLog
//        )
//    }
//    //TODO: 이것도 enum 으로 옮겨
//    private fun isBoss(monster: Monster): Boolean {
//        return monster.type == MonsterType.BOSS
//    }
//
//
//    private fun appendBattleLog(battleLog: StringBuffer, attacker: Combatant, defender: Combatant) {
//        battleLog.append(
//            "${attacker.name} 이(가) ${defender.name} (이)에게 ${attacker.atkDmg} 의 피해를 입혔습니다.\n"
//        )
//    }
//
//    private fun appendDieLog(battleLog: StringBuffer, defender: Combatant) {
//        battleLog.append(
//            "${defender.name} 이(가) 쓰러졌습니다.\n\n"
//        )
//    }
//}