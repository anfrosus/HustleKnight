package com.hustleknight.app.service

import com.hustleknight.app.exception.CustomException
import com.hustleknight.app.exception.ErrorCode

class ServiceUtil {
    companion object {
        fun checkAuth(playerId: Long, idFromToken: Long) {
            if (playerId != idFromToken) {
                throw CustomException(ErrorCode.PLAYER_NOT_MATCH)
            }
        }
    }
}