package com.hustleknight.app.controller

import com.hustleknight.app.dto.request.UpgradeRequest
import com.hustleknight.app.dto.response.PlayerGoodsResponseDto
import com.hustleknight.app.dto.response.PlayerItemResponseDto
import com.hustleknight.app.dto.response.PlayerResponseDto
import com.hustleknight.app.infra.security.UserPrincipal
import com.hustleknight.app.service.ItemService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@Tag(name = "아이템")
@RestController
@RequestMapping("/api/players")
class ItemController(
    private val itemService: ItemService
    ) {
    @Operation(summary = "장착/해제 하기")
    @PutMapping("/{playerId}/items/{itemId}")
    fun equip(
        @PathVariable playerId: Long,
        @PathVariable itemId: Long,
        @AuthenticationPrincipal userPrincipal: UserPrincipal
    ): ResponseEntity<PlayerResponseDto> {
        val response = itemService.updateEquipped(playerId, itemId, userPrincipal.id)
            return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(response)
    }

    //TODO: 합치기
    @Operation(summary = "인벤토리")
    @GetMapping("/{playerId}/items")
    fun getInventory(
        @PathVariable playerId: Long,
        @AuthenticationPrincipal userPrincipal: UserPrincipal
        ): ResponseEntity<List<PlayerItemResponseDto>> {
        val response = itemService.getInventory(playerId, userPrincipal.id)
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(response)
    }

    //TODO: 합치기
    @Operation(summary = "장비창")
    @GetMapping("/{playerId}/items/1")
    fun getEquipped(@PathVariable playerId: Long): ResponseEntity<List<PlayerItemResponseDto>> {
        val result = itemService.getEquipped(playerId)
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(result)
    }

    //TODO: QueryParam으로 받아도 되지않나?
    @Operation(summary = "강화하기")
    @PutMapping("/{playerId}/items/{itemId}/upgrades")
    fun upgrade(
        @PathVariable playerId: Long,
        @PathVariable itemId: Long,
        @RequestBody upgradeRequest: UpgradeRequest,
        @AuthenticationPrincipal userPrincipal: UserPrincipal
    ): ResponseEntity<PlayerItemResponseDto> {
        val result = itemService.upgradeItem(playerId, itemId, upgradeRequest, userPrincipal.id)
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(result)
    }

    @Operation(summary = "분해하기")
    @PutMapping("/{playerId}/items/{itemId}/dismantling")
    fun disassemble(
        @PathVariable playerId: Long,
        @PathVariable itemId: Long,
        @AuthenticationPrincipal userPrincipal: UserPrincipal
    ): ResponseEntity<PlayerGoodsResponseDto>{
        val result = itemService.disassemble(playerId, itemId, userPrincipal.id)
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(result)
    }
}