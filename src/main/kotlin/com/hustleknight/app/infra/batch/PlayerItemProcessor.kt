package com.hustleknight.app.infra.batch

import com.hustleknight.app.model.Player
import com.hustleknight.app.model.enums.PlayerRole
import com.hustleknight.app.model.enums.RaidTier
import org.springframework.batch.item.ItemProcessor
import org.springframework.stereotype.Component

@Component
class PlayerItemProcessor : ItemProcessor<Player, Player>{
    override fun process(player: Player): Player {

        when (player.raidRank) {
            in 1L..10L -> {
                player.earnGold(1_000_000)
            }
            in 11L..100L -> {
                player.earnGold(500_000)
            }
            else -> {

            }
        }

        when (player.raidTier) {
            RaidTier.BRONZE -> {}
            RaidTier.SILVER -> {}
            else -> {}
        }

//        player.resetScore()
        player.resetTicket()
        return player
    }
    /**
     * 랭킹 별 차등 지급.
     * 23시에 랭킹 집계 후 00시 보상지급?
     * 복잡하게 할 필요 없이 보상은 레이드 칠 때 주고 티켓만 초기화?
     **/
}