package com.woozy.untitled.dto.response

import com.woozy.untitled.model.InstancedItem

data class InstancedItemResponseDto(
    var name: String,
    var attrName: String,
    var attrValue: Long
) {
    companion object{
        fun fromEntity(instancedItem: InstancedItem): InstancedItemResponseDto{
            return InstancedItemResponseDto(instancedItem.name, instancedItem.attrName, instancedItem.finalAttrValue)
        }
    }
}
