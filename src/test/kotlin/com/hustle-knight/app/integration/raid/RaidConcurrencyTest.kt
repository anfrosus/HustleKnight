package com.hustleknight.app.integration.raid

import com.hustleknight.app.integration.IntegrationTest
import com.hustleknight.app.repository.PlayerRepository
import com.hustleknight.app.repository.RaidMonsterRepository
import com.hustleknight.app.service.BattleService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors

class RaidConcurrencyTest @Autowired constructor(
    private val battleService: BattleService,
    private val raidMonsterRepository: RaidMonsterRepository,
    private val playerRepository: PlayerRepository
): IntegrationTest() {

    @Test
    @DisplayName("1의 피해를 주는 플레이어 네명이 동시에 레이드를 수행했을 때")
    fun raidConcurrencyTest() {
        //given
        val numberOfThread = 4
        val executor = Executors.newFixedThreadPool(numberOfThread)
        val latch = CountDownLatch(4)

        //when
        executor.execute {
            battleService.raid(1, 1,1)
            latch.countDown()
        }
        executor.execute {
            battleService.raid(2, 2,1)
            latch.countDown()
        }
        executor.execute {
            battleService.raid(3, 3,1)
            latch.countDown()
        }
        executor.execute {
            battleService.raid(4, 4,1)
            latch.countDown()
        }
        latch.await()

        //then
        val result = raidMonsterRepository.findByIdOrNull(1)!!
        val player = playerRepository.findByIdOrNull(1)!!
        assertEquals(996, result.totalHp)
        assertEquals(2, player.ticket)
    }
}