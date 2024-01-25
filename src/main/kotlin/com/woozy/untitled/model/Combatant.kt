package com.woozy.untitled.model

class Combatant {
    var name: String
    var atkDmg: Long
    var hitPnt: Long
    var isPlayer: Boolean

    constructor(player: Player) {
        name = player.name
        atkDmg = player.atkDmg
        hitPnt = player.hitPnt
        isPlayer = true
    }

    constructor(monster: Monster) {
        name = monster.name
        atkDmg = monster.atkDmg
        hitPnt = monster.hitPnt
        isPlayer = false
    }

    fun attack(enemy: Combatant) {
        if (atkDmg <= 0){
            throw Exception()
        }
        enemy.hitPnt -= atkDmg
    }
}