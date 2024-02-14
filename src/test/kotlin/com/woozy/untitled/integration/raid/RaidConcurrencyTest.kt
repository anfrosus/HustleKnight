package com.woozy.untitled.integration.raid

import com.woozy.untitled.integration.IntegrationTest
import com.woozy.untitled.repository.PlayerRepository
import com.woozy.untitled.repository.RaidMonsterRepository
import com.woozy.untitled.service.BattleService
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

//    @BeforeEach
//    fun init() {
//
//    }

    @Test
    @DisplayName("레이드 동시성 테스트")
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