package com.hustleknight.app.repository

import com.hustleknight.app.model.PlayerGoods
import com.hustleknight.app.model.enums.GoodsCategory
import org.springframework.data.jpa.repository.JpaRepository

interface PlayerGoodsRepository : JpaRepository<PlayerGoods, Long> {
    fun findPlayerGoodsByPlayerId(playerId: Long): List<PlayerGoods>

}
