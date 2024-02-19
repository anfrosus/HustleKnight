package com.hustleknight.app.repository

import com.hustleknight.app.model.PlayerItem
import com.hustleknight.app.model.enums.ItemCategory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface PlayerItemRepository: JpaRepository<PlayerItem, Long> {

    fun findByPlayerId(playerId: Long) : List<PlayerItem>

    @Query("select i from PlayerItem i where i.player.id = :playerId AND i.isEquipped = true")
    fun findByPlayerIdAndEquippedIsTrue(playerId: Long) : List<PlayerItem>

    @Query("select i from PlayerItem i where i.player.id = :playerId AND i.category = :category AND i.isEquipped = true")
    fun findByPlayerIdAndCategory(playerId: Long, category: ItemCategory) : PlayerItem?

}