package com.woozy.untitled.model

import com.woozy.untitled.model.enums.PlayerRoleEnum
import com.woozy.untitled.service.BattleResult
import jakarta.persistence.*

@Entity
@Table(name = "PLAYER")
class Player(
    @Column(name = "PLAYER_EMAIL")
    var email: String,

    @Column(name = "PLAYER_PASSWORD")
    var password: String,

    @Column(name = "PLAYER_NAME")
    var name: String,

    @Column(name = "PLAYER_ROLE")
    @Enumerated(value = EnumType.STRING)
    var role: PlayerRoleEnum,

    @Column(name = "PLAYER_LEVEL")
    var level: Long = 1L,

    @Column(name = "PLAYER_AD")
    var atkDmg: Long = 5L,

    // time per attack
    @Column(name = "PLAYER_AS")
    var atkSpd: Double = 5.0,

    @Column(name = "PLAYER_HP")
    var hitPnt: Long = 10L,

    @Column(name = "PLAYER_MAX_STAGE")
    var maxStage: Long = 1L,

    @Column(name = "PLAYER_CUR_STAGE")
    var curStage: Long = 1L,

    @Column(name = "PLAYER_EXP")
    var exp: Long = 0L,

    @Column(name = "PLAYER_GOLD")
    var gold: Long = 0L,

) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    fun earnRewards(battleResult: BattleResult){
            exp += battleResult.droppedExp
            gold += battleResult.droppedGold
    }

    fun goNextStage(battleResult: BattleResult) {
        if (battleResult.stage > maxStage){
            //TODO: EXCEPTION
            throw Exception()
        }else if(battleResult.stage == maxStage){
            maxStage++
            curStage = maxStage
        }
    }
}