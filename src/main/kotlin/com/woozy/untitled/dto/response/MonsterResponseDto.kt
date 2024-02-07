package com.woozy.untitled.dto.response

import com.woozy.untitled.model.Monster
import com.woozy.untitled.model.enums.MonsterType

data class MonsterResponseDto(
    var type: MonsterType,

    var name: String,

    var level: Long,

    var atkDmg: Long,

    var hitPnt: Long,

    var expReward: Long,

    var goldReward: Long,
) {
    companion object {
        fun fromEntity(monster: Monster): MonsterResponseDto {
            return MonsterResponseDto(
                type = monster.type,
                name = monster.name,
                level = monster.level,
                atkDmg = monster.atkDmg,
                hitPnt = monster.hitPnt,
                expReward = monster.expReward,
                goldReward = monster.goldReward
            )
        }
    }
}
