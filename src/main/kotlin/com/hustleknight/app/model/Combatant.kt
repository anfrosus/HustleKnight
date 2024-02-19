package com.hustleknight.app.model

import com.hustleknight.app.exception.CustomException
import com.hustleknight.app.exception.ErrorCode

class Combatant {
    var name: String
    var atkDmg: Long
    var hitPnt: Long
    var isPlayer: Boolean

    constructor(player: Player) {
        name = player.name
        atkDmg = player.atkDmg + player.addiAtkDmg
        hitPnt = player.hitPnt + player.addiHitPnt
        isPlayer = true
    }

    constructor(monster: Monster) {
        name = monster.name
        atkDmg = monster.atkDmg
        hitPnt = monster.hitPnt
        isPlayer = false
    }

    fun attack(enemy: Combatant, battleLog: StringBuffer) {
        if (atkDmg <= 0){
            throw CustomException(ErrorCode.PLAYER_STATS_CAN_NOT_MINUS)
        }
        if (enemy.hitPnt < atkDmg){
            enemy.hitPnt = 0
        }else {
            enemy.hitPnt -= atkDmg
        }
        battleLog.append("${this.name} 이/(가) ${enemy.name} 에게 ${this.atkDmg} 의 피해를 입혔습니다.")
        battleLog.appendLine(" ${enemy.name} 의 체력이 ${enemy.hitPnt} 남았습니다.")
    }

    fun isEnemyDied(enemy: Combatant, battleLog: StringBuffer): Boolean{
        if (enemy.hitPnt <= 0){
            battleLog.appendLine("${enemy.name} 이/(가) 쓰러졌습니다!")
            return true
        }
        return false
    }

}