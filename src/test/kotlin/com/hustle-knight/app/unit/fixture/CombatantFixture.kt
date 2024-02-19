package com.hustleknight.app.unit.fixture

import com.hustleknight.app.model.Combatant

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