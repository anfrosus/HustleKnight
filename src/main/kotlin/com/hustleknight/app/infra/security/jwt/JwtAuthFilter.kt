package com.hustleknight.app.infra.security.jwt

import com.fasterxml.jackson.databind.ObjectMapper
import com.hustleknight.app.infra.security.ApiInfo
import com.hustleknight.app.infra.security.UserPrincipal
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.JwtException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtAuthFilter(
    private val jwtPlugin: JwtPlugin
) : OncePerRequestFilter() {

    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        ApiInfo.entries.forEach {
            if (it.uri == request.requestURI && it.method.toString() == request.method){
                return true
            }
        }
        return false
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val token = jwtPlugin.getTokenFromHeader(request)
        if (!token.isNullOrBlank()) {
            jwtPlugin.validateToken(token.toString())
                .onSuccess { claims ->
                    setAuthentication(claims, request)
                }
                .onFailure { exception ->
                    filterExceptionHandler(exception, response)
                    return
                }
        }
        filterChain.doFilter(request, response)
    }

    private fun setAuthentication(claims: Jws<Claims>, request: HttpServletRequest) {
        val principal = UserPrincipal(
            id = claims.payload.subject.toLong(),
            role = claims.payload["rol"].toString()
        )
        //유저 정보 획득 후 Authentication 객체생성
        val authentication = JwtAuthenticationToken(
            principal = principal,
            details = WebAuthenticationDetailsSource().buildDetails(request)
        )

        //security Context 에 set
        SecurityContextHolder.getContext().authentication = authentication
    }


    private fun filterExceptionHandler(exception: Throwable, response: HttpServletResponse) {
        response.status = HttpStatus.UNAUTHORIZED.value()
        response.contentType = MediaType.APPLICATION_JSON_VALUE
        response.characterEncoding = "UTF-8"
        if (exception is JwtException) {
            response.writer.write(
                ObjectMapper().writeValueAsString(
                    //TODO: ErrorResponse 로 바꾸기 쓰일지는 모르겠으나
                    ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(exception.message)

                )
            )
        } else {
            response.writer.write(
                ObjectMapper().writeValueAsString("뭔지몰랑" + exception.message)
            )
        }
    }
}
