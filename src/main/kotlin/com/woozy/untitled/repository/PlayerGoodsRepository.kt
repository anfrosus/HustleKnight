package com.woozy.untitled.repository

import com.woozy.untitled.model.PlayerGoods
import com.woozy.untitled.model.enums.GoodsCategory
import org.springframework.data.jpa.repository.JpaRepository

interface PlayerGoodsRepository : JpaRepository<PlayerGoods, Long> {
    fun findPlayerGoodsByPlayerId(playerId: Long): List<PlayerGoods>

    fun findByPlayerIdAndGoods_Category(playerId: Long, category: GoodsCategory): PlayerGoods?
}
