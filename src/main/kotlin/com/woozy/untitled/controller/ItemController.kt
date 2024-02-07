package com.woozy.untitled.controller

import com.woozy.untitled.dto.request.UpgradeRequest
import com.woozy.untitled.dto.response.PlayerGoodsResponseDto
import com.woozy.untitled.dto.response.PlayerItemResponseDto
import com.woozy.untitled.dto.response.PlayerResponseDto
import com.woozy.untitled.infra.rabbit.MessagePublisher
import com.woozy.untitled.infra.redis.RedisService
import com.woozy.untitled.infra.security.UserPrincipal
import com.woozy.untitled.service.BattleService
import com.woozy.untitled.service.ItemService
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
    private val itemService: ItemService,
    private val messagePublisher: MessagePublisher,
    private val battleService: BattleService,
    private val redisService: RedisService
    ) {
    @GetMapping("/test")
    fun test(){
//        messagePublisher.sendDelayedMessage(4000, "메세징>_<")
    }

//    @GetMapping("test1")
//    fun test1(){
//        redisService.saveBattleInfo()
//        redisService.getValue()
//    }

    @Operation(summary = "장착/해제 하기")
    @PutMapping("/{playerId}/items/{itemId}")
    fun equip(
        @PathVariable playerId: Long,
        @PathVariable itemId: Long,
        @AuthenticationPrincipal userPrincipal: UserPrincipal
    ): ResponseEntity<PlayerResponseDto> {
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(itemService.updateEquipped(playerId, itemId, userPrincipal))
    }

    //TODO: 합치기
    @Operation(summary = "인벤토리")
    @GetMapping("/{playerId}/items")
    fun getInventory(
        @PathVariable playerId: Long,
        @AuthenticationPrincipal userPrincipal: UserPrincipal
        ): ResponseEntity<List<PlayerItemResponseDto>> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(
                itemService.getInventory(playerId, userPrincipal)
            )
    }

    //TODO: 합치기
    @Operation(summary = "장비창")
    @GetMapping("/{playerId}/items/1")
    fun getEquipped(@PathVariable playerId: Long): ResponseEntity<List<PlayerItemResponseDto>> {
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(
                itemService.getEquipped(playerId)
            )
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
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(itemService.upgradeItem(playerId, itemId, upgradeRequest, userPrincipal))
    }

    @Operation(summary = "분해하기")
    @PutMapping("/{playerId}/items/{itemId}/dismantling")
    fun dismantle(
        @PathVariable playerId: Long,
        @PathVariable itemId: Long,
        @AuthenticationPrincipal userPrincipal: UserPrincipal
    ): ResponseEntity<PlayerGoodsResponseDto>{
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(itemService.deletePlayerItemAndCreatePlayerGoods(playerId, itemId, userPrincipal))
    }
}