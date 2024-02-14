package com.woozy.untitled.unit.model

import com.woozy.untitled.model.RaidMonster
import com.woozy.untitled.unit.fixture.MonsterFixture
import com.woozy.untitled.unit.fixture.PlayerFixture
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

class RaidMonsterUnitTest {
    @Test
    @DisplayName("레이드")
    fun raid() {
        //given
        val player = PlayerFixture.getPlayerBase()
            .apply {
                atkDmg = 10
            }
        val givenHp = 100L
        val monster = MonsterFixture.getRaidMonsterBase()
            .apply { totalHp = givenHp }

        //when
        monster.underAttack(player)

        //then
        assertThat(monster.totalHp).isLessThan(givenHp)
    }
}