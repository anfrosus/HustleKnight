package com.woozy.untitled.service

import com.woozy.untitled.exception.CustomException
import com.woozy.untitled.exception.ErrorCode

class ServiceUtil {
    companion object {
        fun checkAuth(playerId: Long, idFromToken: Long) {
            if (playerId != idFromToken) {
                throw CustomException(ErrorCode.PLAYER_NOT_MATCH)
            }
        }
    }
}