package com.woozy.untitled.unit.model

import com.woozy.untitled.exception.CustomException
import com.woozy.untitled.exception.ErrorCode
import com.woozy.untitled.model.Combatant
import com.woozy.untitled.unit.fixture.CombatantFixture
import com.woozy.untitled.unit.fixture.MonsterFixture
import com.woozy.untitled.unit.fixture.PlayerFixture
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class CombatantUnitTest {
    @Test
    @DisplayName("Combatant 생성 시 player 와 monster 모두 알맞게 생성되어야 함")
    fun createCombatant() {
        //given
        val givenPlayerName = "플레이어1"
        val givenPlayerAd = 10L
        val givenPlayerHp = 20L
        val player = PlayerFixture.getPlayerBase()
            .apply {
                name = givenPlayerName
                atkDmg = givenPlayerAd
                hitPnt = givenPlayerHp
            }
        val givenMonsterName = "몬스터1"
        val givenMonsterAd = 5L
        val givenMonsterHp = 30L
        val monster = MonsterFixture.getMonsterBase()
            .apply {
                name = givenMonsterName
                atkDmg = givenMonsterAd
                hitPnt = givenMonsterHp
            }

        //when
        val combatantPlayer = Combatant(player)
        val combatantMonster = Combatant(monster)

        //then
        assertEquals(true, combatantPlayer.isPlayer)
        assertEquals(givenPlayerName, combatantPlayer.name)
        assertEquals(givenPlayerAd, combatantPlayer.atkDmg)
        assertEquals(givenPlayerHp, combatantPlayer.hitPnt)
        assertEquals(false, combatantMonster.isPlayer)
        assertEquals(givenMonsterName, combatantMonster.name)
        assertEquals(givenMonsterAd, combatantMonster.atkDmg)
        assertEquals(givenMonsterHp, combatantMonster.hitPnt)
    }

    @Test
    @DisplayName("공격력이 음수인 채로 공격을 하면 예외를 던짐")
    fun attackWithMinusAd() {
        //given
        val combatantPlayer = CombatantFixture.getCombatPlayer()
            .apply { atkDmg = - 1 }
        val combatantMonster = CombatantFixture.getCombatMonster()
        val battleLong = StringBuffer()

        //when
        val e = assertThrows<CustomException> {
            combatantPlayer.attack(combatantMonster, battleLong)
        }

        //then
        assertEquals(ErrorCode.PLAYER_STATS_CAN_NOT_MINUS, e.errorCode)

    }

    @Test
    @DisplayName("공격 시 체력이 알맞게 닳고 알맞은 로그가 추가되어야함")
    fun test() {
        //given
        val givenAd = 10L
        val givenHp = 30L
        val expectedHp = 20L
        val combatPlayer = CombatantFixture.getCombatPlayer()
            .apply { atkDmg = givenAd }
        val combatMonster = CombatantFixture.getCombatMonster()
            .apply { hitPnt = givenHp }
        val battleLog = StringBuffer()
        val expectedLog =
            "${combatPlayer.name} 이/(가) ${combatMonster.name} 에게 ${combatPlayer.atkDmg} 의 피해를 입혔습니다." +
                    " ${combatMonster.name} 의 체력이 $expectedHp 남았습니다.\n"


        //when
        combatPlayer.attack(combatMonster, battleLog)

        //then
        assertEquals(expectedHp, combatMonster.hitPnt)
        assertEquals(expectedLog, battleLog.toString())
    }

    @Test
    @DisplayName("적이 죽었다면 true 를 return 하고 알맞은 로그를 추가해야함. 적의 체력은 0 이되어야함")
    fun checkEnemyDied() {
        //given
        val givenAd = 10L
        val givenHp = 5L
        val expectedHp = 0L
        val combatPlayer = CombatantFixture.getCombatPlayer()
            .apply { atkDmg = givenAd }
        val combatMonster = CombatantFixture.getCombatMonster()
            .apply { hitPnt = givenHp }
        val battleLog = StringBuffer()
        val expectedLog = "${combatMonster.name} 이/(가) 쓰러졌습니다!\n"
        //when
        combatPlayer.attack(combatMonster, battleLog)
        battleLog.setLength(0)
        val result1 = combatMonster.isEnemyDied(combatPlayer, battleLog)
        val result2 = combatPlayer.isEnemyDied(combatMonster, battleLog)

        //then
        assertEquals(true, result2)
        assertEquals(false, result1)
        assertEquals(expectedLog, battleLog.toString())
        assertEquals(expectedHp, combatMonster.hitPnt)
    }
}
