package com.woozy.untitled.dto.response

import com.woozy.untitled.model.Item
import com.woozy.untitled.model.PlayerItem
import com.woozy.untitled.model.enums.ItemCategory

data class PlayerItemResponseDto(
    var category: ItemCategory,
    var name: String,
    var attrName: String,
    var finalAttrValue: Long,
    var successCnt: Int,
    var remainingCnt: Int,
    var isEquipped: Boolean
) {
    companion object {
        fun fromEntity(playerItem: PlayerItem): PlayerItemResponseDto {
            return PlayerItemResponseDto(
                category = playerItem.category,
                name = playerItem.name,
                attrName = playerItem.attrName,
                finalAttrValue = playerItem.finalAttrValue,
                successCnt = playerItem.successCnt,
                remainingCnt = playerItem.remainingCnt,
                isEquipped = playerItem.isEquipped
            )
        }

        fun fromItem(item: Item): PlayerItemResponseDto {
            return PlayerItemResponseDto(
                category = item.category,
                name = item.name,
                attrName = item.attrName,
                finalAttrValue = item.attrValue,
                successCnt = 0,
                remainingCnt = item.category.upgradeableCnt,
                isEquipped = false
            )
        }
    }
}
