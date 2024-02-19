package com.hustleknight.app.infra.security.jwt

import com.hustleknight.app.model.Player
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.time.Duration
import java.time.Instant
import java.util.*
import javax.crypto.SecretKey

@Component
class JwtPlugin(
    @Value("\${auth.jwt.secret.key}") private val SECRET_KEY: String,
    @Value("\${auth.jwt.exp.access}") private val accessTokenExpirationHour: Long,
    @Value("\${auth.jwt.exp.refresh}") private val refreshTokenExpirationHour: Long
) {

    companion object {
        private const val BEARER_PREFIX = "Bearer "
    }

    private val key: SecretKey = Keys.hmacShaKeyFor(SECRET_KEY.toByteArray(StandardCharsets.UTF_8))

    fun validateToken(accessToken: String): Result<Jws<Claims>> {
        return runCatching {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(accessToken)
        }
    }

    fun generateAccessToken(player: Player, response: HttpServletResponse) {
        val generatedToken = generateToken(player, Duration.ofHours(accessTokenExpirationHour))
        setTokenAtHeaderWithBearer(response, generatedToken)
    }

    fun generateRefreshToken(player: Player): String {
        return generateToken(player, Duration.ofHours(refreshTokenExpirationHour))
    }

    fun getTokenFromHeader(request: HttpServletRequest): String? {
        val bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION)
            ?: return null
        return substringToken(bearerToken)
    }


    private fun substringToken(bearerToken: String): String {
        if (!bearerToken.startsWith(BEARER_PREFIX)) {
            return ""
        }
        return bearerToken.substring(7)
    }

    fun claimToSet(inputString: String): Set<String> {
        val result = inputString
            .trim('[', ']')
            .split(",")
            .map { it.trim() }
            .toSet()
        return result
    }

    private fun generateToken(player: Player, expiration: Duration): String {
        val now = Instant.now()
        Jwts.parser()

        return Jwts.builder()
            .id(UUID.randomUUID().toString())
            .issuer("Woozy")
            .issuedAt(Date.from(now))
            .expiration(Date.from(now.plus(expiration)))
            .subject(player.id.toString())
            .claim("rol", player.role)
            .signWith(key)
            .compact()
    }

    fun setTokenAtHeaderWithBearer(response: HttpServletResponse, token: String) {
        response.setHeader(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + token)
    }
}