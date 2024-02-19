package com.hustleknight.app.infra.security

import com.fasterxml.jackson.databind.ObjectMapper
import com.hustleknight.app.exception.CustomException
import com.hustleknight.app.exception.ErrorCode
import com.hustleknight.app.exception.ErrorResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component

@Component
class AuthenticationEntryPointException: AuthenticationEntryPoint {
    //security 인증객체가 필요한데 없을 때
    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException
    ) {
        val e = CustomException(ErrorCode.UNAUTHORIZED)
        response.status = HttpStatus.UNAUTHORIZED.value()
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.characterEncoding = "UTF-8"
        response.writer.write(
            ObjectMapper().writeValueAsString(ErrorResponse(e)))
    }
}