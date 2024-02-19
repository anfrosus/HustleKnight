package com.woozy.untitled.controller

import com.woozy.untitled.infra.sse.SseService
import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter

@RestController
class SseController(
    private val sseService: SseService
) {

    @Operation(summary = "사냥터 가기 (SSE connect)")
    @GetMapping("/sse/{playerId}")
    fun connect(@PathVariable playerId: Long): SseEmitter {
        return sseService.createEmitter(playerId)
    }

    @Operation(summary = "사냥터 나가기 (SSE disconnect)")
    @DeleteMapping("/sse/{playerId}")
    fun disconnect(@PathVariable playerId: Long): ResponseEntity<String> {
        sseService.deleteEmitter(playerId)
        return ResponseEntity
            .status(HttpStatus.OK)
            .body("Disconnect")
    }

    @Operation(summary = "연결 확인")
    @GetMapping("/sse/checking/{playerId}")
    fun check(@PathVariable playerId: Long) {
        sseService.checkHasEmitter(playerId)
    }
}