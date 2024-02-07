package com.woozy.untitled.service

import com.woozy.untitled.dto.request.UpgradeRequest
import com.woozy.untitled.dto.response.PlayerGoodsResponseDto
import com.woozy.untitled.dto.response.PlayerItemResponseDto
import com.woozy.untitled.dto.response.PlayerResponseDto
import com.woozy.untitled.exception.CustomException
import com.woozy.untitled.exception.ErrorCode
import com.woozy.untitled.infra.security.UserPrincipal
import com.woozy.untitled.repository.PlayerGoodsRepository
import com.woozy.untitled.repository.PlayerItemRepository
import com.woozy.untitled.repository.PlayerRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ItemService(
    private val playerRepository: PlayerRepository,
    private val playerItemRepository: PlayerItemRepository,
    private val playerGoodsRepository: PlayerGoodsRepository
) {

    //TODO: 인벤토리랑 equipped 합쳐
    @Transactional(readOnly = true)
    fun getInventory(playerId: Long, userPrincipal: UserPrincipal): List<PlayerItemResponseDto> {
        ServiceUtil.checkPlayerId(playerId, userPrincipal)
        val inventory = playerItemRepository.findPlayerItemsByPlayerId(playerId)
        return inventory.map { PlayerItemResponseDto.fromEntity(it) }.toList()
    }

    @Transactional(readOnly = true)
    fun getEquipped(playerId: Long): List<PlayerItemResponseDto> {
        //TODO: checkPlayer
        val equipped = playerItemRepository.findPlayerItemsByPlayerIdAndEquippedIsTrue(playerId)
        return equipped.map {
            PlayerItemResponseDto.fromEntity(it)
        }.toList()
    }

    @Transactional
    fun updateEquipped(playerId: Long, selectedItemId: Long, userPrincipal: UserPrincipal): PlayerResponseDto {
        ServiceUtil.checkPlayerId(playerId, userPrincipal)
        val player = playerRepository.findByIdOrNull(playerId)
            ?: throw CustomException(ErrorCode.PLAYER_NOT_FOUND)
        //isEquipped 와 category 로 먼저장착한 아이템이 있는지 확인
        val selectedItem = playerItemRepository.findByIdOrNull(selectedItemId)
            ?: throw CustomException(ErrorCode.ITEM_NOT_FOUND)
        //해제라면 ? selectedItem 이 isEquipped 면 해제하면 되지 않나
        if (selectedItem.isEquipped) {
            player.takeOff(selectedItem)
        } else {
            //장착이라면? 이미끼고있는 아이템이 있다면 해제
            val equippedItem = playerItemRepository.findPlayerItemsByPlayerIdAndEquippedIsTrue(playerId)
            equippedItem.forEach {
                if (selectedItem.type == it.type) {
                    player.takeOff(it)
                }
            }
            player.putOn(selectedItem)

        }
        return PlayerResponseDto.fromEntity(player)
    }

    @Transactional
    fun upgradeItem(
        playerId: Long,
        itemId: Long,
        upgradeRequest: UpgradeRequest,
        userPrincipal: UserPrincipal
    ): PlayerItemResponseDto {
        ServiceUtil.checkPlayerId(playerId, userPrincipal)
        //TODO: isEquipped의 exception / upgrade 같은 logic Entity 내부에??
        val selectedItem = playerItemRepository.findByIdAndPlayerId(itemId, playerId)
            ?: throw CustomException(ErrorCode.ITEM_NOT_FOUND)
        val player = playerRepository.findByIdOrNull(playerId)
            ?: throw CustomException(ErrorCode.PLAYER_NOT_FOUND)

        val upgradeCategory = upgradeRequest.upgradeDetail

        selectedItem.checkUpgradable(upgradeCategory.itemCategory)
        player.consumeGoods(selectedItem.reqLevel, upgradeCategory)

        selectedItem.upgrade(upgradeCategory)
        return PlayerItemResponseDto.fromEntity(selectedItem)
    }

    @Transactional
    fun deletePlayerItemAndCreatePlayerGoods(playerId: Long, itemId: Long, userPrincipal: UserPrincipal): PlayerGoodsResponseDto {
        ServiceUtil.checkPlayerId(playerId, userPrincipal)
        val selectedItem = playerItemRepository.findByIdAndPlayerId(itemId, playerId)
            ?: throw CustomException(ErrorCode.ITEM_NOT_FOUND)

        selectedItem.checkUnequipped()

        val playerGoods =
            playerGoodsRepository.findByPlayerIdAndGoods_Category(playerId, selectedItem.category.reqStone)
                ?:throw CustomException(ErrorCode.PLAYER_GOODS_NOT_FOUND)

        //삭제하고 강화석 얻고
        playerItemRepository.delete(selectedItem)
        playerGoods.decrease(selectedItem.stoneWhenDismantle())
        return PlayerGoodsResponseDto.fromEntity(playerGoods)
    }

//    private fun consumeGoods(item: PlayerItem, playerGoodsList: List<PlayerGoods>, itemUpgradeCategory: ItemUpgradeCategory) {
//        val consumeStoneAmount = item.reqLevel * itemUpgradeCategory.consumePrefix * 10
//        val consumeGoldAmount = item.reqLevel * itemUpgradeCategory.consumePrefix * 150
//        playerGoodsList.forEach {
//            if (itemUpgradeCategory.requiredGoods == it.goods.category) {
//                if (it.amount - consumeStoneAmount < 0 || it.player.gold - consumeGoldAmount < 0) {
//                    throw CustomException(ErrorCode.UPGRADE_GOODS_NOT_ENOUGH, "필요 강화석: ${consumeStoneAmount}, 필요 골드: ${consumeGoldAmount}")
//                }
//                it.amount -= consumeStoneAmount
//                it.player.gold -= consumeGoldAmount
//            }
//        }
//    }

}
