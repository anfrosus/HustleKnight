package com.hustleknight.app.unit.service

import com.hustleknight.app.infra.rabbit.MessagePublisher
import com.hustleknight.app.infra.redis.RedisService
import com.hustleknight.app.model.Goods
import com.hustleknight.app.model.GoodsDropTable
import com.hustleknight.app.model.PlayerGoods
import com.hustleknight.app.model.enums.GoodsCategory
import com.hustleknight.app.model.enums.MaxValues
import com.hustleknight.app.repository.MonsterRepository
import com.hustleknight.app.repository.PlayerItemRepository
import com.hustleknight.app.repository.PlayerRepository
import com.hustleknight.app.repository.RaidMonsterRepository
import com.hustleknight.app.service.BattleService
import com.hustleknight.app.unit.fixture.MonsterFixture
import com.hustleknight.app.unit.fixture.PlayerFixture
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class BattleServiceTest {
    private val playerRepository: PlayerRepository = mockk()
    private val monsterRepository: MonsterRepository = mockk()
    private val playerItemRepository: PlayerItemRepository = mockk()
    private val raidMonsterRepository: RaidMonsterRepository = mockk()
    private val messagePublisher: MessagePublisher = mockk()
    private val redisService: RedisService = mockk()
    private val battleService: BattleService =
        BattleService(playerRepository, monsterRepository, playerItemRepository, raidMonsterRepository ,messagePublisher, redisService)

    @Test
    @DisplayName("플레이어가 승리했을 때 기대보상은 몬스터의 보상과 일치해야함")
    fun playerWin() {
        //given
        val player = PlayerFixture.getPlayerBase()
            .apply {
                id = 1L
                atkDmg = 10
                addiAtkDmg = 5
                atkSpd = 10
                hitPnt = 100
            }
        val expectedGold = 10L
        val expectedExp = 20L
        val monster = MonsterFixture.getMonsterBase()
            .apply {
                id = 1L
                atkDmg = 1
                hitPnt = 50
                goldReward = expectedGold
                expReward = expectedExp
            }

        val expectedTurn = 4
        val expectedTime = 20L

        //when
        val result = battleService.battle(player, monster)

        //then
        assertEquals(true, result.hasPlayerWon)
        assertEquals(expectedTurn, result.turn)
        assertEquals(expectedTime * 1000, result.delayTimeMilliSeconds)
        assertEquals(expectedExp, result.expectedExp)
        assertEquals(expectedGold, result.expectedGold)
    }

    @Test
    @DisplayName("플레이어가 패배했을 때 기대보상은 0임")
    fun playerLose() {
        //given
        val player = PlayerFixture.getPlayerBase()
            .apply {
                id = 1L
                atkDmg = 10
                hitPnt = 30
            }
        val monster = MonsterFixture.getMonsterBase()
            .apply {
                id = 1L
                atkDmg = 10
                hitPnt = 500
                goldReward = 10
                expReward = 20
            }
        val expectedTurn = 3

        //when
        val result = battleService.battle(player, monster)

        //then
        assertEquals(expectedTurn, result.turn)
        assertEquals(false, result.hasPlayerWon)
        assertEquals(0, result.expectedGold)
        assertEquals(0, result.expectedExp)
    }

    @Test
    @DisplayName("20턴안에 끝나지 않으면 몬스터가 승리해야 하고 기대보상은 0 이여야함")
    fun turnOver() {
        //given
        val player = PlayerFixture.getPlayerBase()
            .apply {
                id = 1L
                atkDmg = 1
                addiAtkDmg = 0
                hitPnt = 300
            }
        val monster = MonsterFixture.getMonsterBase()
            .apply {
                id = 1L
                atkDmg = 1
                hitPnt = 500
            }
        val expectedTurn = MaxValues.MAX_TURN.value

        //when
        val result = battleService.battle(player, monster)

        //then
        assertEquals(false, result.hasPlayerWon)
        assertEquals(expectedTurn, result.turn)
        assertEquals(0, result.expectedGold)
    }

//    @Test
//    @DisplayName("예상시간이 xx 초 이상이면 player 가 패배")
}