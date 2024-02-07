package com.woozy.untitled.repository

import com.woozy.untitled.model.Player
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface PlayerRepository: JpaRepository<Player, Long> {
    fun findByEmail(email: String): Player?

    fun existsByEmail(email: String) :Boolean

    @Query("select p from Player p left join fetch p.playerGoodsList where p.id = :playerId")
    fun findByIdFetchPlayerGoods(playerId: Long): Player?
}
