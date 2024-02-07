package com.woozy.untitled.infra.redis

import com.woozy.untitled.dto.BattleInfo
import com.woozy.untitled.exception.CustomException
import com.woozy.untitled.exception.ErrorCode
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RedisService(
    private val redisTemplate: RedisTemplate<Long, BattleInfo>
) {

    fun saveBattleInfo(battleInfo: BattleInfo) {
        println(battleInfo.battleId)
        when (redisTemplate.opsForValue().setIfAbsent(battleInfo.battleId, battleInfo)) {
            true -> {
                println("저장성공!!!!")
            }
            //이미 키가 저장되어 있을 때
            false -> {
                println("이미 키가있으!!!!")
                //TODO: Change Exception
                throw CustomException(ErrorCode.MONSTER_NOT_FOUND)
            }
            //TODO: change Exception
            null -> throw CustomException(ErrorCode.PLAYER_NOT_FOUND)
        }
    }

    fun getBattleInfo(battleId: Long): BattleInfo {
        val result = redisTemplate.opsForValue().get(battleId)
        //TODO: change Exception
            ?: throw CustomException(ErrorCode.MONSTER_NOT_FOUND)

        return result
    }

    fun checkBattleInProgress(battleId: Long) {

//        redisTemplate.exec()
        if (redisTemplate.hasKey(battleId)) {
            throw CustomException(ErrorCode.ITEM_EQUIPPED)
            //TODO: changeException
        }

    }

    fun deleteBattleInfo(battleId: Long) {
        if (!redisTemplate.delete(battleId)) {
            //TODO: ChangeException
            throw CustomException(ErrorCode.PLAYER_NOT_FOUND)
        }
    }

    @Transactional(readOnly = true)
    fun getValue() {
        val result = redisTemplate.opsForValue().get(1L)
        //TODO: change Exception
            ?: throw CustomException(ErrorCode.MONSTER_NOT_FOUND)
    }


}