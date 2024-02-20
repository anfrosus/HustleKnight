package com.hustleknight.app.unit.fixture

import com.hustleknight.app.model.Player
import com.hustleknight.app.model.PlayerItem
import com.hustleknight.app.model.enums.ItemCategory

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