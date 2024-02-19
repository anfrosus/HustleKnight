package com.woozy.untitled.unit.fixture

import com.woozy.untitled.model.Combatant

class CombatantFixture {
    companion object {
        fun getCombatPlayer(): Combatant {
            return Combatant(PlayerFixture.getPlayerBase())
        }

        fun getCombatMonster(): Combatant{
            return Combatant(MonsterFixture.getMonsterBase())
        }
    }
}