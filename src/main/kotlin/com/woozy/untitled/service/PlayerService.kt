package com.woozy.untitled.service

import com.woozy.untitled.dto.request.HuntRequestDto
import com.woozy.untitled.dto.request.PlayerRequestDto
import com.woozy.untitled.dto.request.toEntity
import com.woozy.untitled.dto.response.HuntResponseDto
import com.woozy.untitled.dto.response.InstancedItemResponseDto
import com.woozy.untitled.dto.response.PlayerResponseDto
import com.woozy.untitled.model.*
import com.woozy.untitled.model.enums.MonsterTypeEnum
import com.woozy.untitled.repository.DropTableRepository
import com.woozy.untitled.repository.InstancedItemRepository
import com.woozy.untitled.repository.MonsterRepository
import com.woozy.untitled.repository.PlayerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.random.Random

@Service
class PlayerService(
    private val playerRepository: PlayerRepository,
    private val monsterRepository: MonsterRepository,
    private val dropTableRepository: DropTableRepository,
    private val instancedItemRepository: InstancedItemRepository
) {
    private val monsterRandomRate = 0.05

    @Transactional
    fun createPlayer(playerRequestDto: PlayerRequestDto): PlayerResponseDto {
        //TODO: password encode
        val newPlayer = playerRepository.save(playerRequestDto.toEntity())
        return PlayerResponseDto.fromEntity(newPlayer)
    }

    //    @Transactional
    suspend fun hunt(huntRequestDto: HuntRequestDto, playerId: Long, idFromToken: Long): HuntResponseDto {
        val verifiedId = checkPlayerId(playerId, idFromToken)
        val battleResult: BattleResult
        //TODO: CustomException
        val player = playerRepository.findByIdOrNull(verifiedId)
            ?: throw Exception()
        val monster = monsterRepository.findByStageAndType(player.curStage, randomTypeByRate(monsterRandomRate))
            ?: throw Exception()
        println(monster.name + "!!!!!!!!!!!!!!")
        // 배틀이랑 같은 tx에 있으면 안될 듯
        withContext(Dispatchers.IO) {
            battleResult = battle(player, monster)
            applyBattleResult(battleResult, player, monster)
        }

        return HuntResponseDto(
            battleResult.droppedExp,
            battleResult.droppedGold,
            battleResult.droppedItemList,
            battleResult.battleLog.toString()
        )

    }

    // 트랜잭션을 물고 있는 시간을 최소화하기 위해 battle 이후 데이터를 저장할 때만 @Transactional 활성화
    @Transactional
    fun applyBattleResult(
        battleResult: BattleResult,
        player: Player,
        monster: Monster
    ) {
        if (battleResult.isPlayerWon) {
            // 골드와 경험치 획득
            player.earnRewards(battleResult)
            //스테이지 확인 후 넘어가게 해주기?
            player.goNextStage(battleResult)
            // 드랍테이블 ㄱ
            // monster <-> dropTable 연관관계 설정
//            val dropTable = dropTableRepository.findByMonsterId(monster.id!!)
            val droppedItemList = getDroppedItemList(monster.dropTable)
            println(droppedItemList.size)
            //createItemInstance
            val instancedItemList = createItemInstance(player, droppedItemList)
            battleResult.addAllInstancedItem(instancedItemList)

            playerRepository.save(player)
            if (instancedItemList.isNotEmpty()) {
                instancedItemRepository.saveAll(instancedItemList)
            }

        }
    }

    fun createItemInstance(player: Player, droppedItemList: List<Item>): List<InstancedItem> {
        return droppedItemList.map {
            InstancedItem(
                player,
                it.name,
                it.attrName,
                attrWithOption(it.attrValue),
                it.type,
                it.category,
                remainingCnt = 7
            )
        }.toList()
    }

    private fun attrWithOption(attrValue: Long): Long {
        val result = attrValue + (Random.nextInt(11) - 5)
        return result
    }

    private fun getDroppedItemList(dropTable: List<DropTable>): List<Item> {
        println("${dropTable.size} !?!?!?")
        return dropTable.mapNotNull {
            if (Random.nextDouble() < it.dropRate) {
                println("dropped!!!!")
                it.item
            } else {
                null
            }
        }.toList()
    }

    private fun checkPlayerId(requestId: Long, idFromToken: Long): Long {
        if (requestId != idFromToken) {
            //TODO: CustomException
            throw Exception()
        }
        return requestId
    }

    private fun randomTypeByRate(rate: Double): MonsterTypeEnum {
        val randomDouble = Random.nextDouble()
        val result =
            when {
                randomDouble < rate -> {
                    MonsterTypeEnum.MUTANT
                }

                else -> {
                    MonsterTypeEnum.NORMAL
                }
            }

        return result
    }

    private suspend fun battle(player: Player, monster: Monster): BattleResult {
        //레벨이 높은쪽이 선공?
        //체력 - 공격력 했을 때 0보다 작거나 같으면 뒤짐
        //초당 공격속도 이므로 몇번 반복해야하는지 횟수가 초가되겠네
        val playerAtkSpd = player.atkSpd
        val firstAttacker = if (player.level >= monster.level) Combatant(player) else Combatant(monster)
        val secondAttacker = if (firstAttacker.isPlayer) Combatant(monster) else Combatant(player)
        val winner: Combatant
        val battleLog = StringBuffer()
        var limitCnt = 0

        println("전투시작!!")

        while (true) {

            delay((playerAtkSpd * 1000).toLong())
            firstAttacker.attack(secondAttacker)
            appendBattleLog(battleLog, firstAttacker, secondAttacker)
            if (secondAttacker.hitPnt <= 0) {
                appendDieLog(battleLog, secondAttacker)
                winner = firstAttacker
                break
            }

            secondAttacker.attack(firstAttacker)
            appendBattleLog(battleLog, secondAttacker, firstAttacker)
            if (firstAttacker.hitPnt <= 0) {
                appendDieLog(battleLog, firstAttacker)
                winner = secondAttacker
                break
            }
            println("전투중!!")

            if (limitCnt++ > 20) {
                winner = Combatant(monster)
                break
            }

        }
        return BattleResult(winner.isPlayer, monster.expReward, monster.goldReward, monster.stage, battleLog)
    }

    private fun appendBattleLog(battleLog: StringBuffer, attacker: Combatant, defender: Combatant) {
        battleLog.append(
            "${attacker.name} 이(가) ${defender.name} (이)에게 ${attacker.atkDmg} 의 피해를 입혔습니다.\n"
        )
    }

    private fun appendDieLog(battleLog: StringBuffer, defender: Combatant) {
        battleLog.append(
            "${defender.name} 이(가) 쓰러졌습니다.\n\n"
        )
    }


}
