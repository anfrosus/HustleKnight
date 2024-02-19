package com.woozy.untitled.dto.request

import com.woozy.untitled.model.Player
import com.woozy.untitled.model.enums.PlayerRole
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern

data class PlayerRequestDto(
    @field:Email(message = "이메일 형식을 확인해 주세요")
    val email: String,

    @field:NotNull
    @field:Pattern(regexp = "^[a-zA-Z0-9~!@#$%^&*()_+=-]{4,10}$", message = "최소 4자 이상, 10자 이하이며 알파벳 대소문자(a~z, A~Z), 특수문자로 구성되어야 한다.")
    val password: String,
    val name: String
)

fun PlayerRequestDto.toEntity(encodedPassword: String): Player{
    return Player(
        email = this.email,
        password = encodedPassword,
        name = this.name,
        role = PlayerRole.REGULAR
    )
}

