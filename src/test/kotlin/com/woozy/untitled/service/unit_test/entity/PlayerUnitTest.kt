package com.woozy.untitled.service.unit_test.entity

import com.woozy.untitled.exception.CustomException
import com.woozy.untitled.exception.ErrorCode
import com.woozy.untitled.model.GoodsDropTable
import com.woozy.untitled.model.Monster
import com.woozy.untitled.model.Player
import com.woozy.untitled.model.PlayerItem
import com.woozy.untitled.model.enums.MonsterType
import com.woozy.untitled.model.enums.PlayerRole
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class PlayerUnitTest {
    private val goodsDropTable: GoodsDropTable = mockk()

    //addCurrency굳이?
    @Test
    fun 골드와_경험치_획득() {
        //given
        val dropGold = 10L
        val dropExp = 10L
        val monster =
            Monster(MonsterType.NORMAL, 1, "테스트몬스터", 1, 1, 1, dropExp, dropGold, goodsDropTable = goodsDropTable)
        val player = Player("tester@email.com", "testpwd", "tester", PlayerRole.REGULAR)

        //when
        player.earnRewards(monster)

        //then
        assertTrue(player.gold == 10L)
        assertTrue(player.exp == 10L)

    }

    //분기 별 테스트
    @Test
    fun 보스몬스터_처치시_다음_스테이지로() {
        //given
        val bossMonster = Monster(MonsterType.BOSS, 1, "테스트몬스터", 1, 1, 1, 10, 10, goodsDropTable = goodsDropTable)
        val player = Player("tester@email.com", "testpwd", "tester", PlayerRole.REGULAR)

        //when
        player.goNextStageIfBoss(bossMonster)
        //then
        assertTrue(player.curStage == 2L)
        assertTrue(player.maxStage == 2L)
    }

    @Test
    fun 몬스터의_스테이지가_최고_스테이지_보다_높을_때_예외를_발생() {
        //given
        val playerMaxStage = 2L
        val monsterStage = 3L
        val monster =
            Monster(MonsterType.BOSS, monsterStage, "테스트몬스터", 1, 1, 1, 10, 10, goodsDropTable = goodsDropTable)
        val player = Player("tester@email.com", "testpwd", "tester", PlayerRole.REGULAR, maxStage = playerMaxStage)

        //when
        val e = assertThrows<CustomException> {
            player.goNextStageIfBoss(monster)
        }
        //then
        assertEquals(e.errorCode.message, ErrorCode.PLAYER_ILLEGAL_STAGE.message)
    }

    @Test
    fun 최고_스테이지의_몬스터지만_보스가_아닐_때_스테이지가_변동되지_않아야_함() {
        //given
        val notBossMonster = Monster(MonsterType.NORMAL, 1, "테스트몬스터", 1, 1, 1, 10, 10, goodsDropTable = goodsDropTable)
        val player = Player("tester@email.com", "testpwd", "tester", PlayerRole.REGULAR)

        //when
        player.goNextStageIfBoss(notBossMonster)
        //then
        assertTrue(player.curStage == 1L)
        assertTrue(player.maxStage == 1L)
    }

//    @Test
//    @DisplayName("장비를 벗을 때 알맞게 능력치가 적용되어야 함")
//    fun takeOff(){
//        //given
//        val player = Player("tester@email.com", "testpwd", "tester", PlayerRole.REGULAR)
//        val playerItem = PlayerItem(player, "테스트 장비", )
//    }

}