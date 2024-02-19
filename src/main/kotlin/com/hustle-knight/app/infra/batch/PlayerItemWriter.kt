package com.woozy.untitled.infra.batch

import com.woozy.untitled.model.Player
import com.woozy.untitled.repository.PlayerRepository
import org.springframework.batch.item.Chunk
import org.springframework.batch.item.ItemWriter
import org.springframework.stereotype.Component

@Component
class PlayerItemWriter(
    private val playerRepository: PlayerRepository
): ItemWriter<Player> {
    val count = 1L

    override fun write(chunk: Chunk<out Player>) {
//        chunk.map {
//            it.emdtn = count
//        }
        playerRepository.saveAll(chunk.items)
    }
}