package com.woozy.untitled.infra.security

import org.springframework.http.HttpMethod

enum class ApiInfo(val method: HttpMethod, val uri: String) {
    REGISTER_PLAYER(HttpMethod.POST, "/api/players"),
    LOGIN(HttpMethod.POST, "/api/players/login"),
    REISSUE(HttpMethod.POST, "/api/players/{playerId}/reissue"),
//    SUSPEND(HttpMethod.POST, "/api/players/2/hunts?continue")
}