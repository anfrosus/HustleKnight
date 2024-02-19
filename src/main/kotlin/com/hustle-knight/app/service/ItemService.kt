package com.woozy.untitled.service

import com.woozy.untitled.dto.request.UpgradeRequest
import com.woozy.untitled.dto.response.PlayerGoodsResponseDto
import com.woozy.untitled.dto.response.PlayerItemResponseDto
import com.woozy.untitled.dto.response.PlayerResponseDto
import com.woozy.untitled.exception.CustomException
import com.woozy.untitled.exception.ErrorCode
import com.woozy.untitled.infra.security.UserPrincipal
import com.woozy.untitled.repository.PlayerItemRepository
import com.woozy.untitled.repository.PlayerRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ItemService(
    private val playerRepository: PlayerRepository,
    private val playerItemRepository: PlayerItemRepository,
) {

    //TODO: 인벤토리랑 equipped 합쳐
    @Transactional(readOnly = true)
    fun getInventory(playerId: Long, idFromToken: Long): List<PlayerItemResponseDto> {
        ServiceUtil.checkAuth(playerId, idFromToken)
        val inventory = playerItemRepository.findByPlayerId(playerId)
        return inventory.map { PlayerItemResponseDto.fromEntity(it) }.toList()
    }

    @Transactional(readOnly = true)
    fun getEquipped(playerId: Long): List<PlayerItemResponseDto> {
        //TODO 합쳐질 것
        val equipped = playerItemRepository.findByPlayerIdAndEquippedIsTrue(playerId)
        return equipped.map {
            PlayerItemResponseDto.fromEntity(it)
        }.toList()
    }

    @Transactional
    fun updateEquipped(playerId: Long, selectedItemId: Long, idFromToken: Long): PlayerResponseDto {
        ServiceUtil.checkAuth(playerId, idFromToken)
        val player = playerRepository.findByIdOrNull(playerId)
            ?: throw CustomException(ErrorCode.PLAYER_NOT_FOUND)

        // isEquipped 와 category 로 먼저장착한 아이템이 있는지 확인
        val selectedItem = playerItemRepository.findByIdOrNull(selectedItemId)
            ?: throw CustomException(ErrorCode.ITEM_NOT_FOUND)

        // 장착 해제
        if (selectedItem.isEquipped) {
            player.takeOff(selectedItem)
            return PlayerResponseDto.fromEntity(player)
        }

        val equippedItem = playerItemRepository.findByPlayerIdAndCategory(playerId, selectedItem.category)
        if (equippedItem != null) {
            player.takeOff(equippedItem)
        }
        player.putOn(selectedItem)

        return PlayerResponseDto.fromEntity(player)
    }

    @Transactional
    fun upgradeItem(
        playerId: Long,
        itemId: Long,
        upgradeRequest: UpgradeRequest,
        idFromToken: Long
    ): PlayerItemResponseDto {
        ServiceUtil.checkAuth(playerId, idFromToken)
        val selectedItem = playerItemRepository.findByIdOrNull(itemId)
            ?: throw CustomException(ErrorCode.ITEM_NOT_FOUND)

        val upgradeCategory = upgradeRequest.upgradeDetail
        selectedItem.checkUpgradable(upgradeCategory.itemCategory)

        val player = playerRepository.findByIdFetchPlayerGoods(playerId)
            ?: throw CustomException(ErrorCode.PLAYER_NOT_FOUND)

        val playerGoods = player.playerGoodsList.first {
            selectedItem.category.reqStone == it.goods.category
        }

        playerGoods.consume(selectedItem.reqLevel, upgradeCategory)

        selectedItem.upgrade(upgradeCategory)
        return PlayerItemResponseDto.fromEntity(selectedItem)
    }

    @Transactional
    fun disassemble(
        playerId: Long,
        itemId: Long,
        idFromToken: Long
    ): PlayerGoodsResponseDto {
        ServiceUtil.checkAuth(playerId, idFromToken)
        val selectedItem = playerItemRepository.findByIdOrNull(itemId)
            ?: throw CustomException(ErrorCode.ITEM_NOT_FOUND)
        selectedItem.checkUnequipped()

        val player = playerRepository.findByIdFetchPlayerGoods(playerId)
            ?: throw CustomException(ErrorCode.PLAYER_NOT_FOUND)

        val playerGoods = player.playerGoodsList.first {
            selectedItem.category.reqStone == it.goods.category
        }

        //삭제하고 강화석 얻고
        playerGoods.increase(selectedItem.getStoneWhenDisassemble())
        playerItemRepository.delete(selectedItem)

        return PlayerGoodsResponseDto.fromEntity(playerGoods)
    }

}
