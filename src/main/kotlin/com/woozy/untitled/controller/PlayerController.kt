package com.woozy.untitled.controller

import com.woozy.untitled.dto.request.HuntRequestDto
import com.woozy.untitled.dto.request.LoginRequestDto
import com.woozy.untitled.dto.request.PlayerRequestDto
import com.woozy.untitled.dto.response.HuntingResponseDto
import com.woozy.untitled.dto.response.PlayerGoodsResponseDto
import com.woozy.untitled.dto.response.PlayerResponseDto
import com.woozy.untitled.dto.response.RaidResponseDto
import com.woozy.untitled.infra.security.UserPrincipal
import com.woozy.untitled.service.BattleService
import com.woozy.untitled.service.PlayerService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@Tag(name = "플레이어")
@RestController
@RequestMapping("/api/players")
class PlayerController(
    private val playerService: PlayerService,
    private val battleService: BattleService
) {

    @Operation(summary = "플레이어 등록하기")
    @PostMapping
    fun registerPlayer(
        @RequestBody @Valid playerRequestDto: PlayerRequestDto
    ): ResponseEntity<PlayerResponseDto> {
        val result = playerService.createPlayer(playerRequestDto)
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(result)
    }

    @Operation(summary = "로그인")
    @PostMapping("/login")
    fun login(
        @RequestBody loginRequestDto: LoginRequestDto,
        response: HttpServletResponse
    ): ResponseEntity<PlayerResponseDto> {
        val result = playerService.login(loginRequestDto, response)
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(result)
    }

    @Operation(summary = "플레이어 정보 / 스탯창")
    @GetMapping("/{playerId}")
    fun getPlayer(
        @PathVariable playerId: Long,
        @AuthenticationPrincipal userPrincipal: UserPrincipal
    ): ResponseEntity<PlayerResponseDto> {
        val result = playerService.getPlayer(playerId, userPrincipal.id)
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(result)
    }

    @Operation(summary = "플레이어 재화 조회")
    @GetMapping("/{playerId}/goods")
    fun getPlayerGoods(
        @PathVariable playerId: Long,
        @AuthenticationPrincipal userPrincipal: UserPrincipal
    ): ResponseEntity<List<PlayerGoodsResponseDto>>{
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(playerService.getPlayerGoods(playerId, userPrincipal.id))
    }

    @Operation(summary = "사냥하기")
    @PostMapping("/{playerId}/hunts")
//    @PreAuthorize("hasRole('REGULAR')")
    fun hunt(
        @RequestBody huntRequestDto: HuntRequestDto,
        @PathVariable playerId: Long,
        @AuthenticationPrincipal userPrincipal: UserPrincipal
    ): ResponseEntity<HuntingResponseDto> {
        val result = battleService.hunt(huntRequestDto, playerId, userPrincipal.id)
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(result)
    }

    @Operation(summary = "전투 중단")
    @PutMapping("/{playerId}/hunts")
    fun abort(
        @PathVariable playerId: Long,
        @AuthenticationPrincipal userPrincipal: UserPrincipal
    ): ResponseEntity<String> {
        val result = battleService.abort(playerId, userPrincipal.id)
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(result)
    }

    @Operation(summary = "")
    @PostMapping("{playerId}/raid/{raidMonsterId}")
    fun raid(@PathVariable playerId: Long,
             @PathVariable raidMonsterId: Long,
             @AuthenticationPrincipal userPrincipal: UserPrincipal
    ): ResponseEntity<RaidResponseDto>{
        val result = battleService.raid(playerId, raidMonsterId, userPrincipal.id)
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(result)
    }
}