package com.woozy.untitled.dto.response

import com.woozy.untitled.model.Player
import com.woozy.untitled.model.enums.PlayerRoleEnum

//
//abstract class Domain<D, E> {
//    fun toEntity() {
//        return E
//    }
//    fun fromEntity() {
//        return D
//    }
//}


data class PlayerResponseDto(
    val email: String,
    val name: String,
    val role: PlayerRoleEnum,
) {
    companion object {
        fun fromEntity(player: Player): PlayerResponseDto {
            return PlayerResponseDto(
                email = player.email,
                name = player.name,
                role = player.role
            )
        }
    }
}
