package com.woozy.untitled.service

import com.woozy.untitled.dto.request.PlayerRequestDto
import com.woozy.untitled.dto.request.toEntity
import com.woozy.untitled.dto.response.PlayerResponseDto
import com.woozy.untitled.repository.PlayerRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PlayerService(
    private val playerRepository: PlayerRepository
) {

    @Transactional
    fun createPlayer(playerRequestDto: PlayerRequestDto): PlayerResponseDto {
        //TODO: password encode
        val newPlayer = playerRepository.save(playerRequestDto.toEntity())
        return PlayerResponseDto.fromEntity(newPlayer)
    }

}
