package com.woozy.untitled.model

import com.woozy.untitled.model.enums.PlayerRoleEnum
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

    @Column(name = "PLAYER_AS")
    var attSpd: Double = 1.0,

    @Column(name = "PLAYER_HP")
    var hitPnt: Long = 10L,

    @Column(name = "PLAYER_AD")
    var attDmg: Long = 5L,

    @Column(name = "PLAYER_MAX_STAGE")
    var maxStage: Long = 1L,

    @Column(name = "PLAYER_CUR_STAGE")
    var curStage: Long = 1L,

    @Column(name = "PLAYER_EXP")
    var exp: Long = 0L

) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
}