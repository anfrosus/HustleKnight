package com.woozy.untitled.service

import com.woozy.untitled.exception.CustomException
import com.woozy.untitled.exception.ErrorCode
import com.woozy.untitled.infra.security.UserPrincipal
import com.woozy.untitled.model.PlayerItem

class ServiceUtil {
    companion object {
        fun checkPlayerId(playerId: Long, userPrincipal: UserPrincipal) {
            if (playerId != userPrincipal.id) {
                throw CustomException(ErrorCode.PLAYER_NOT_MATCH)
            }
        }
    }
}