package com.woozy.untitled.dto.response

data class HuntResponseDto(
    val earnedExp: Long,

    val earnedGold: Long,

    val droppedItemList: List<InstancedItemResponseDto>,

    val battleLog: String,
) {

}
