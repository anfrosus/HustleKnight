package com.woozy.untitled.repository

import com.woozy.untitled.model.PlayerItem
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface PlayerItemRepository: JpaRepository<PlayerItem, Long> {

    fun findPlayerItemsByPlayerId(playerId: Long) : MutableList<PlayerItem>

    fun findByIdAndPlayerId(itemId: Long, playerId: Long) : PlayerItem?

    @Query("select i from PlayerItem i where i.player.id = :playerId AND i.isEquipped = true")
    fun findPlayerItemsByPlayerIdAndEquippedIsTrue(playerId: Long) : List<PlayerItem>
}