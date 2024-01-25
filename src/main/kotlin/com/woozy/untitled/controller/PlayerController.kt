package com.woozy.untitled.controller

import com.woozy.untitled.dto.request.HuntRequestDto
import com.woozy.untitled.dto.request.PlayerRequestDto
import com.woozy.untitled.dto.response.HuntResponseDto
import com.woozy.untitled.dto.response.PlayerResponseDto
import com.woozy.untitled.service.PlayerService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/players")
class PlayerController(
    private val playerService: PlayerService
) {
    @PostMapping
    fun registerPlayer(
        @RequestBody playerRequestDto: PlayerRequestDto
    ): ResponseEntity<PlayerResponseDto> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(playerService.createPlayer(playerRequestDto))
    }

    @PostMapping("{playerId}/hunts")
    suspend fun hunt(
        @RequestBody huntRequestDto: HuntRequestDto,
        @PathVariable playerId: Long,
//        idFromToken: Long
        ): ResponseEntity<HuntResponseDto>{
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(playerService.hunt(huntRequestDto, playerId, 1))
    }
}