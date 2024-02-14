package com.woozy.untitled.dto.response

data class HuntingResponseDto(
    val hasPlayerWon: Boolean,
    val expectedTimeSeconds: Long,
    val encounteredMonster: String,
    val expectedGold: Long,
    val expectedExp: Long,
    val expectedDrop: DropResponseDto,
) {

}
