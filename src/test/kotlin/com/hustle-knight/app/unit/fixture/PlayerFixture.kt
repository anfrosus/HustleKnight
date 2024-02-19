package com.woozy.untitled.unit.fixture

import com.woozy.untitled.model.Goods
import com.woozy.untitled.model.Player
import com.woozy.untitled.model.PlayerGoods
import com.woozy.untitled.model.enums.GoodsCategory
import com.woozy.untitled.model.enums.PlayerRole

class PlayerFixture {
    companion object {
        fun getPlayerBase(): Player {
            return Player("tester@email.com", "testpwd", "tester", PlayerRole.REGULAR)
        }

        fun getPlayerWithGoodsList(): Player {
            val player = getPlayerBase()
            val redStone = Goods(GoodsCategory.RED_STONE, "무기 강화")
            val blueStone = Goods(GoodsCategory.BLUE_STONE, "장신구 강화")
            val givenPlayerGoodsList = mutableListOf(
                PlayerGoods(player, redStone, redStone.category, 0),
                PlayerGoods(player, blueStone, blueStone.category, 0)
            )
            player.playerGoodsList = givenPlayerGoodsList
            return player
        }
    }
}

