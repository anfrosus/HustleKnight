package com.woozy.untitled.service

import com.woozy.untitled.dto.request.LoginRequestDto
import com.woozy.untitled.dto.request.PlayerRequestDto
import com.woozy.untitled.dto.request.toEntity
import com.woozy.untitled.dto.response.PlayerGoodsResponseDto
import com.woozy.untitled.dto.response.PlayerResponseDto
import com.woozy.untitled.exception.CustomException
import com.woozy.untitled.exception.ErrorCode
import com.woozy.untitled.infra.security.UserPrincipal
import com.woozy.untitled.infra.security.jwt.JwtPlugin
import com.woozy.untitled.model.Player
import com.woozy.untitled.model.PlayerGoods
import com.woozy.untitled.repository.GoodsRepository
import com.woozy.untitled.repository.PlayerGoodsRepository
import com.woozy.untitled.repository.PlayerRepository
import jakarta.servlet.http.HttpServletResponse
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PlayerService(
    private val playerRepository: PlayerRepository,
    private val playerGoodsRepository: PlayerGoodsRepository,
    private val goodsRepository: GoodsRepository,
    private val jwtPlugin: JwtPlugin,
    private val passwordEncoder: PasswordEncoder
) {
    @Transactional
    fun createPlayer(playerRequestDto: PlayerRequestDto): PlayerResponseDto {
        if (playerRepository.existsByEmail(playerRequestDto.email)){
            throw CustomException(ErrorCode.PLAYER_EMAIL_ALREADY_EXIST)
        }
        val encodedPassword = passwordEncoder.encode(playerRequestDto.password)
        val newPlayer = playerRequestDto.toEntity(encodedPassword)
        val playerCurrency = createCurrency(newPlayer)
        playerRepository.save(newPlayer)
        playerGoodsRepository.saveAll(playerCurrency)
        return PlayerResponseDto.fromEntity(newPlayer)
    }

    @Transactional
    fun login(loginRequestDto: LoginRequestDto, response: HttpServletResponse): PlayerResponseDto {
        val player = playerRepository.findByEmail(loginRequestDto.email)
            ?: throw CustomException(ErrorCode.PLAYER_NOT_FOUND)
        if (!passwordEncoder.matches(loginRequestDto.password, player.password)) {
            throw CustomException(ErrorCode.PLAYER_PASSWORD_NOT_MATCH)
        }
        //TODO: refreshToken 발급
        jwtPlugin.generateAccessToken(player, response)
        return PlayerResponseDto.fromEntity(player)
    }

    @Transactional(readOnly = true)
    fun getPlayer(playerId: Long, idFromToken: Long): PlayerResponseDto {
        ServiceUtil.checkAuth(playerId, idFromToken)
        val player = playerRepository.findByIdOrNull(playerId)
            ?: throw CustomException(ErrorCode.PLAYER_NOT_FOUND)
        return PlayerResponseDto.fromEntity(player)
    }

    @Transactional(readOnly = true)
    fun getPlayerGoods(playerId: Long, idFromToken: Long): List<PlayerGoodsResponseDto> {
        ServiceUtil.checkAuth(playerId, idFromToken)
        val playerGoodsList = playerGoodsRepository.findPlayerGoodsByPlayerId(playerId)
        return playerGoodsList.map {
            PlayerGoodsResponseDto.fromEntity(it)
        }.toList()
    }

    private fun createCurrency(player: Player): List<PlayerGoods> {
        val goodsList = goodsRepository.findAll()
        return goodsList.map {
            PlayerGoods(
                player = player,
                goods = it,
                category = it.category,
                amount = 0
            )
        }.toList()

    }




}
