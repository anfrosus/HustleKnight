package com.hustleknight.app.repository

import com.hustleknight.app.model.Player
import com.hustleknight.app.model.enums.RaidTier
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface PlayerRepository : JpaRepository<Player, Long> {
    fun findByEmail(email: String): Player?

    fun existsByEmail(email: String): Boolean

    @Query("select p from Player p left join fetch p.playerGoodsList where p.id = :playerId")
    fun findByIdFetchPlayerGoods(playerId: Long): Player?

    fun findByRaidScoreGreaterThan(raidScore: Long): MutableList<Player>

    fun findByRaidTierIsIn(tierList: MutableList<RaidTier>): MutableList<Player>
}
