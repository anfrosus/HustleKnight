//package com.woozy.untitled.dto
//
//import com.woozy.untitled.dto.response.PlayerGoodsResponseDto
//import com.woozy.untitled.dto.response.PlayerItemResponseDto
//import com.woozy.untitled.model.PlayerGoods
//import com.woozy.untitled.model.PlayerItem
//
//data class BattleResult(
//    val isBoss: Boolean,
//    val isPlayerWon: Boolean,
//    val stage: Long,
//    val battleLog: StringBuffer,
//    val droppedItemList: MutableList<PlayerItemResponseDto> = mutableListOf(),
//    val droppedGoods: MutableList<PlayerGoodsResponseDto> = mutableListOf(),
//    var isLevelUp: Boolean = false,
//    var expInfo: String = ""
//
//) {
//    var droppedExp: Long = 0
//    var droppedGold: Long = 0
//    fun addAllPlayerItem(playerItems: List<PlayerItem>) {
//        droppedItemList.addAll(
//            playerItems.map { PlayerItemResponseDto.fromEntity(it) }.toList()
//        )
//    }
//    fun addPlayerGoods(playerGoods: PlayerGoods){
//        droppedGoods.add(PlayerGoodsResponseDto.fromEntity(playerGoods))
//    }
//
//    fun isLevelUp(boolean: Boolean) {
//        isLevelUp = boolean
//    }
//}