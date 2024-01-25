package com.woozy.untitled.service

import com.woozy.untitled.dto.response.InstancedItemResponseDto
import com.woozy.untitled.model.InstancedItem

data class BattleResult(

    val isPlayerWon: Boolean,
    val droppedExp: Long,
    val droppedGold: Long,
    val stage: Long,
    val battleLog: StringBuffer,
    val droppedItemList: MutableList<InstancedItemResponseDto> = mutableListOf()


) {
    fun addAllInstancedItem(instancedItems: List<InstancedItem>) {
        droppedItemList.addAll(
            instancedItems.map { InstancedItemResponseDto.fromEntity(it) }.toList()
        )
    }
}