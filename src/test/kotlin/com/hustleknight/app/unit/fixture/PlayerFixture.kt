package com.hustleknight.app.unit.fixture

import com.hustleknight.app.model.Goods
import com.hustleknight.app.model.Player
import com.hustleknight.app.model.PlayerGoods
import com.hustleknight.app.model.enums.GoodsCategory
import com.hustleknight.app.model.enums.PlayerRole

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

