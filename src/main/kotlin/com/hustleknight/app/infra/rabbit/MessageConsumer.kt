//package com.hustleknight.app.infra.rabbit
//
//import com.hustleknight.app.infra.redis.RedisService
//import com.hustleknight.app.infra.sse.SseService
//import com.hustleknight.app.service.BattleService
//import org.springframework.amqp.rabbit.annotation.RabbitListener
//import org.springframework.stereotype.Service
//
//@Service
//class MessageConsumer(
//    private val redisService: RedisService,
//    private val battleService: BattleService,
//    private val sseService: SseService
//) {
//
//    @RabbitListener(queues = ["test-queue"])
//    fun handleMessage(battleId: Long) {
//        try {
//            val battleInfo = redisService.getBattleInfo(battleId)
//            if (!battleInfo.isStopped) {
//                //전투가 중단되지 않았다면
//                val dropResponseDto = battleService.applyBattleResult(battleInfo)
//                battleInfo.dropped = dropResponseDto
//
//                redisService.checkEmitterExistence(battleId)
//                sseService.pushBattleResult(battleId, battleInfo)
//            }
//        } catch (e: Exception) {
//                //TODO: 실패한 battle에 대한 정보를 따로 저장하던지 의 조치가 필요
////            log.error(e)
//        } finally {
//            redisService.deleteBattleInfo(battleId)
//        }
//
//
//    }
//}
//
