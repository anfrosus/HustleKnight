package com.hustleknight.app.model.enums

import kotlin.random.Random

enum class ItemCategory(val upLimit: Int, val reqStone: GoodsCategory, val upgradeableCnt: Int, val attrName: String) {
    WEAPON(7, GoodsCategory.RED_STONE, 7, "공격력"),
    ACCESSORY(5, GoodsCategory.BLUE_STONE, 5, "공격 속도"),
    ARMOR(10, GoodsCategory.RED_STONE, 10, "체력");

    companion object {
        fun isDropped(dropRate: Double): Boolean {
            return (Random.nextDouble() < dropRate)
        }

        fun attrWithOption(attrValue: Long): Long {
            var result = attrValue + (Random.nextInt(11) - 5)
            if (result <= 0) {
                result = 1
            }
            return result
        }
    }
}

