package com.woozy.untitled.dto

import com.woozy.untitled.model.enums.MonsterType
import java.io.Serializable

data class BattleDto(
    var playerId: Long,
    var monsterId: Long,
    var stage: Long,
    var monsterType: MonsterType,
    var hasPlayerWon: Boolean,
    var delayTimeMilliSeconds: Long,
    var expectedGold: Long,
    var expectedExp: Long,
    var battleLog: String
) {
}