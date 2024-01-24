package com.woozy.untitled.model

import com.woozy.untitled.model.enums.MonsterTypeEnum
import jakarta.persistence.*

@Entity
@Table(name = "MONSTER")
class Monster(

    @Column(name = "MONSTER_NAME")
    @Enumerated(EnumType.STRING)
    var type: MonsterTypeEnum,

    @Column(name = "MONSTER_STAGE")
    var stage: Long,

    @Column(name = "MONSTER_NAME")
    var name: String,

    @Column(name = "MONSTER_LEVEL")
    var level: Long,

    @Column(name = "MONSTER_AD")
    var atkDmg: Long,

    @Column(name = "MONSTER_HP")
    var hitPnt: Long,

    @Column(name = "MONSTER_DROP_EXP")
    var dropExp: Long,

    @Column(name = "MONSTER_DROP_GOLD")
    var dropGold: Long

) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
}