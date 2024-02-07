package com.woozy.untitled.model.enums

import com.woozy.untitled.exception.CustomException
import com.woozy.untitled.exception.ErrorCode
import kotlin.random.Random

enum class ItemCategory(val upLimit: Int, val reqStone: GoodsCategory) {
    WEAPON(7, GoodsCategory.RED_STONE),
    ACCESSORY(5, GoodsCategory.BLUE_STONE),
    ARMOR(10, GoodsCategory.RED_STONE);

    companion object {
        fun isDropped(dropRate: Double): Boolean {
            return (Random.nextDouble() < dropRate)
        }

        fun attrWithOption(attrValue: Long): Long {
            val result = attrValue + (Random.nextInt(11) - 5)
            if (result <= 0) {
                //TODO: ChangeException
                throw CustomException(ErrorCode.ITEM_EQUIPPED)
            }
            return result
        }
    }
}

