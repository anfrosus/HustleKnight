package com.woozy.untitled.dto.request

import com.woozy.untitled.model.Player
import com.woozy.untitled.model.enums.UserRoleEnum

data class PlayerRequestDto(
    //TODO: VALIDATION
    val email: String,
    val password: String,
    val name: String
)

fun PlayerRequestDto.toEntity(): Player{
    return Player(
        email = this.email,
        password = this.password,
        name = this.name,
        role = UserRoleEnum.REGULAR
    )
}

