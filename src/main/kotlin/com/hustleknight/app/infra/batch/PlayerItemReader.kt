package com.hustleknight.app.infra.batch

import com.hustleknight.app.model.Player
import com.hustleknight.app.model.enums.RaidTier
import com.hustleknight.app.repository.PlayerRepository
import org.springframework.batch.item.ItemReader
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
@Component
class PlayerItemReader(
    private val playerRepository: PlayerRepository,
) : ItemReader<Player> {
    var index = 0L
    override fun read(): Player? {
        val playerList = playerRepository.findByRaidTierIsIn(
            mutableListOf(
                RaidTier.BRONZE,
                RaidTier.SILVER,
                RaidTier.GOLD,
                RaidTier.PLATINUM,
                RaidTier.DIAMOND,
                RaidTier.MASTER
            )
        )
        val sortedPlayerList = playerList.sortedByDescending {
            it.raidScore
        }

        val max = playerList.size
        if (index >= max) {
            resetIdx()
            return null
        }
        val result = sortedPlayerList[index++.toInt()].apply {
            raidRank = index + 1
        }
        return result
//        println(index)
//        val player = playerRepository.findByIdOrNull(index++)
//        if (player == null) {
//            resetIdx()
//        }
//        return player
    }

    private fun resetIdx() {
        index = 0
    }
}