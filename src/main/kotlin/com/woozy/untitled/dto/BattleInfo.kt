package com.woozy.untitled.dto

import com.woozy.untitled.dto.BattleDto
import java.io.Serializable

data class BattleInfo(
    val battleId: Long,
    val battleDto: BattleDto,
    val isStopped: Boolean
): Serializable {

    constructor(battleDto: BattleDto): this(battleDto.playerId, battleDto, false){

    }
}