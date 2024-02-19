package com.woozy.untitled.model.enums

import kotlin.random.Random

enum class ItemUpgradeCategory(
    val itemCategory: ItemCategory,
    val rate: Double, val increment: Long, val consumePrefix: Long
) {
    WEAPON10(ItemCategory.WEAPON, 0.1, 5, 3),
    WEAPON60(ItemCategory.WEAPON, 0.6, 2, 2),
    WEAPON100(ItemCategory.WEAPON, 1.0, 1, 1),
    ARMOR10(ItemCategory.ARMOR, 0.1, 10, 4),
    ARMOR60(ItemCategory.ARMOR, 0.6, 4, 2),
    ARMOR100(ItemCategory.ARMOR, 1.0, 2, 1),
    ACCESSORY10(ItemCategory.ACCESSORY, 0.1, 3, 3),
    ACCESSORY60(ItemCategory.ACCESSORY, 0.6, 2, 2),
    ACCESSORY100(ItemCategory.ACCESSORY, 1.0, 1, 1);

    fun getRequireStoneAmount(reqLevel: Long): Long {
        return reqLevel * this.consumePrefix * 5
    }

    fun getRequireGoldAmount(reqLevel: Long): Long {
        return reqLevel * this.consumePrefix * 150
    }

    fun isSuccess(): Boolean {
        return Random.nextDouble() < this.rate
    }
}