package com.woozy.untitled.dto.response

data class HuntResponseDto(
    val hasWon: Boolean,

    val monster: MonsterResponseDto,

    val earnedExp: Long,

    val earnedGold: Long,

    val droppedItemList: List<PlayerItemResponseDto>,

    val droppedGoodsList: List<PlayerGoodsResponseDto>,

    val battleLog: String,

    val isLevelUp: Boolean,

    val expInfo: String
) {

}
