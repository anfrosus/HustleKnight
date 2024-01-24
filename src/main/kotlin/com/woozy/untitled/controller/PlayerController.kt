package com.woozy.untitled.controller

import com.woozy.untitled.dto.request.PlayerRequestDto
import com.woozy.untitled.dto.response.PlayerResponseDto
import com.woozy.untitled.service.PlayerService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController("/api")
class PlayerController(
    private val playerService: PlayerService
) {
    @PostMapping("/players")
    fun registerPlayer(
        @RequestBody playerRequestDto: PlayerRequestDto
    ): ResponseEntity<PlayerResponseDto> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(playerService.createPlayer(playerRequestDto))
    }
}