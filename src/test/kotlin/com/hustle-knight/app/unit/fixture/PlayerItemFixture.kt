package com.woozy.untitled.unit.fixture

import com.woozy.untitled.model.Player
import com.woozy.untitled.model.PlayerItem
import com.woozy.untitled.model.enums.ItemCategory

class PlayerItemFixture {
    companion object {
        fun getItemBase(player: Player): PlayerItem{
            return PlayerItem(
                player,
                ItemCategory.WEAPON,
                "플레이어 아이템1",
                1,
                finalAttrValue = 10L
            )

        }
        fun getItemBase(player: Player, category: ItemCategory, attrValue: Long): PlayerItem {
            return PlayerItem(
                player,
                category,
                "${category}1",
                1,
                finalAttrValue = attrValue
            )
        }
    }
}