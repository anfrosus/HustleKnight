package com.woozy.untitled.infra.security.jwt

import com.woozy.untitled.infra.security.UserPrincipal
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.web.authentication.WebAuthenticationDetails
import java.io.Serializable

class JwtAuthenticationToken(
    private val principal: UserPrincipal,
    details: WebAuthenticationDetails
): AbstractAuthenticationToken(principal.authorities), Serializable {
init {
    //JWT 검증 성공 시 바로 생성할 예정이므로 생성시 authenticated 를 true 로
    super.setAuthenticated(true)
    super.setDetails(details)
}
    override fun getCredentials() = null
    override fun getPrincipal() = principal
}