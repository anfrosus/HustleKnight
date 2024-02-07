package com.woozy.untitled.infra.rabbit

import com.woozy.untitled.infra.redis.RedisService
import com.woozy.untitled.infra.sse.SseController
import com.woozy.untitled.service.BattleService
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Service

@Service
class MessageConsumer(
    private val redisService: RedisService,
    private val battleService: BattleService,
    private val sseController: SseController
) {

    @RabbitListener(queues = ["test-queue"])
    fun handleMessage(battleId: Long) {
        println("컨슘!!!!!!!!!!!!!!!!")
        try {
            val battleInfo = redisService.getBattleInfo(battleId)
            if (!battleInfo.isStopped) {
                //전투가 중단되지 않았다면
                battleService.applyBattleResult(battleInfo)

                //TODO: 이벤트 발생시켜 + 드랍 템도 같이 리턴 해야겠다이
                sseController.pushBattleResult(battleId, battleInfo.battleDto)
            }

            redisService.deleteBattleInfo(battleId)
        } catch (e: Exception) {
            println(battleId)
                //TODO: consume이 실패하지 않도록.
//            log.error(e)
        }


    }
}

