package com.woozy.untitled.dto.response

import com.woozy.untitled.model.PlayerItem
import com.woozy.untitled.model.enums.ItemCategory
import com.woozy.untitled.model.enums.ItemType

data class PlayerItemResponseDto(
    var type: ItemType,
    var category: ItemCategory,
    var name: String,
    var attrName: String,
    var finalAttrValue: Long,
    var successCnt: Int,
    var remainingCnt: Int,
    var isEquipped: Boolean
) {
    companion object{
        fun fromEntity(playerItem: PlayerItem): PlayerItemResponseDto{
            return PlayerItemResponseDto(
                type = playerItem.type,
                category = playerItem.category,
                name = playerItem.name,
                attrName = playerItem.attrName,
                finalAttrValue = playerItem.finalAttrValue,
                successCnt = playerItem.successCnt,
                remainingCnt = playerItem.remainingCnt,
                isEquipped = playerItem.isEquipped)
        }
    }
}
