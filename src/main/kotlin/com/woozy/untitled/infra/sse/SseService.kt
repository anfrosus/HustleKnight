package com.woozy.untitled.infra.sse

import com.woozy.untitled.dto.BattleInfo
import com.woozy.untitled.exception.CustomException
import com.woozy.untitled.exception.ErrorCode
import com.woozy.untitled.infra.redis.RedisService
import com.woozy.untitled.model.enums.MaxValues
import org.springframework.stereotype.Service
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter
import java.io.IOException
import java.util.concurrent.ConcurrentHashMap

@Service
class SseService(
    private val redisService: RedisService
) {

    private val emitterList = ConcurrentHashMap<Long, SseEmitter>()
    fun createEmitter(playerId: Long): SseEmitter {
        val emitter = SseEmitter(MaxValues.MAX_CONNECTION_TIME.value)
        if (this.emitterList[playerId] != null) {
            throw CustomException(ErrorCode.EMITTER_ALREADY_EXIST)
        }
        this.emitterList[playerId] = emitter
        emitter.onTimeout {
            this.emitterList.remove(playerId)
            redisService.deleteEmitterExistence(playerId)
        }
        try {
            emitter.send("Connect")
            redisService.setEmitterExistence(playerId)
        } catch (e: IOException) {
            emitter.completeWithError(e)
            e.printStackTrace()
            //TODO: 아마 throw 해줘야할듯?
        }
        return emitter
    }
    fun deleteEmitter(playerId: Long) {
        val emitter = this.emitterList[playerId]
            ?: throw CustomException(ErrorCode.EMITTER_NOT_FOUND)
        try {
            emitter.send("Close")
            redisService.deleteEmitterExistence(playerId)
        } catch (e: IOException) {
            emitter.completeWithError(e)
            e.printStackTrace()
            //TODO: 아마 throw 해줘야할듯?
        } finally {
            emitter.complete()
            emitterList.remove(playerId)
        }
    }

    fun checkHasEmitter(playerId: Long) {
        if (!this.emitterList.containsKey(playerId)) {
            throw CustomException(ErrorCode.EMITTER_NOT_FOUND)
        }
        val emitter = this.emitterList[playerId]!!
        try {
            emitter.send("Check")
        }catch (e: IOException) {
            emitter.completeWithError(e)
            e.printStackTrace()
        }
    }

    fun pushBattleResult(playerId: Long, battleResult: BattleInfo) {
        val emitter = this.emitterList[playerId]
            ?: throw CustomException(ErrorCode.EMITTER_NOT_FOUND)
        try {
            emitter.send(battleResult)
        } catch (e: IOException) {
            e.printStackTrace()
            //TODO: 아마 throw 해줘야할듯?
        }
    }

}