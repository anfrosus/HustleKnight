package com.woozy.untitled.infra.redis

import com.woozy.untitled.dto.BattleInfo
import com.woozy.untitled.exception.CustomException
import com.woozy.untitled.exception.ErrorCode
import com.woozy.untitled.model.enums.MaxValues
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

@Service
class RedisService(
    private val battleRedisTemplate: RedisTemplate<Long, BattleInfo>,
    private val emitterRedisTemplate: RedisTemplate<String, Boolean>
) {
    fun stopBattle(battleId: Long) {
        val result = this.getBattleInfo(battleId)
        result.isStopped = true
        battleRedisTemplate.opsForValue().set(battleId, result)
    }

    fun saveBattleInfo(battleInfo: BattleInfo) {
        when (battleRedisTemplate.opsForValue().setIfAbsent(
            battleInfo.battleId,
            battleInfo,
            MaxValues.MAX_BATTLE_SAVED_MINUTE.value,
            TimeUnit.MINUTES
        )) {
            true -> {}
            false -> throw CustomException(ErrorCode.REDIS_BATTLE_NOT_COMPLETE)
            null -> throw CustomException(ErrorCode.REDIS_CAN_NOT_SAVE)
        }

    }

    fun getBattleInfo(battleId: Long): BattleInfo {
        val result = battleRedisTemplate.opsForValue().get(battleId)
            ?: throw CustomException(ErrorCode.REDIS_BATTLE_NOT_FOUND)

        return result
    }

    fun checkBattleInProgress(battleId: Long) {
        if (battleRedisTemplate.hasKey(battleId)) {
            throw CustomException(ErrorCode.REDIS_BATTLE_NOT_COMPLETE)
        }
    }

    fun deleteBattleInfo(battleId: Long) {
        if (!battleRedisTemplate.delete(battleId)) {
            throw CustomException(ErrorCode.REDIS_BATTLE_NOT_FOUND)
        }
    }

    fun setEmitterExistence(playerId: Long) {
        val key = emitterKey(playerId)
        when (emitterRedisTemplate.opsForValue().setIfAbsent(key, true)) {
            true -> {}
            false -> throw CustomException(ErrorCode.REDIS_EMITTER_ALREADY_EXIST)
            null -> throw CustomException(ErrorCode.REDIS_CAN_NOT_SAVE)
        }
    }

    fun deleteEmitterExistence(playerId: Long) {
        val key = emitterKey(playerId)
        emitterRedisTemplate.delete(key)
    }

    fun checkEmitterExistence(playerId: Long) {
        val key = emitterKey(playerId)
        if (!emitterRedisTemplate.hasKey(key)) {
            throw CustomException(ErrorCode.EMITTER_NOT_FOUND)
        }
    }

    private fun emitterKey(playerId: Long): String {
        val EMITTER_KEY_PREFIX = "Emitter"
        return EMITTER_KEY_PREFIX + playerId.toString()
    }
}