package com.woozy.untitled.unit.service

import com.woozy.untitled.model.enums.ItemCategory
import com.woozy.untitled.repository.PlayerItemRepository
import com.woozy.untitled.repository.PlayerRepository
import com.woozy.untitled.service.ItemService
import com.woozy.untitled.unit.fixture.PlayerFixture
import com.woozy.untitled.unit.fixture.PlayerItemFixture
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.data.repository.findByIdOrNull

class ItemServiceTest {
    private val playerRepository: PlayerRepository = mockk()
    private val playerItemRepository: PlayerItemRepository = mockk()
    private val itemService: ItemService = ItemService(playerRepository, playerItemRepository)

    @Test
    @DisplayName("선택된 장비가 장착된 상태에서 장착 요청을 보내면 해제해야 함")
    fun takeOffIfEquipped() {
        //given
        val player = PlayerFixture.getPlayerBase()
            .apply {
                id = 1L
                addiAtkDmg = 5
            }
        val givenItem = PlayerItemFixture.getItemBase(player)
            .apply {
                id = 1L
                isEquipped = true
                finalAttrValue = 5
            }
        every { playerRepository.findByIdOrNull(any()) } returns player
        every { playerItemRepository.findByIdOrNull(any()) } returns givenItem

        //when
        itemService.updateEquipped(player.id!!, givenItem.id!!, 1L)

        //then
        assertEquals(false, givenItem.isEquipped)
        assertEquals(0, player.addiAtkDmg)
    }

    @Test
    @DisplayName("선택된 장비가 장착되지 않은 상태에서 장착요청을 보냈을 때 기존장착 아이템이 없는 경우")
    fun putOnIfUnequipped() {
        //given
        val player = PlayerFixture.getPlayerBase()
            .apply {
                id = 1L
                addiAtkDmg = 0
            }
        val givenAttrValue = 5L
        val givenItem = PlayerItemFixture.getItemBase(player)
            .apply {
                id = 1L
                isEquipped = false
                finalAttrValue = givenAttrValue
            }
        every { playerRepository.findByIdOrNull(any()) } returns player
        every { playerItemRepository.findByIdOrNull(any()) } returns givenItem
        every { playerItemRepository.findByPlayerIdAndCategory(any(), any()) } returns null
        //when
        itemService.updateEquipped(player.id!!, givenItem.id!!, 1L)

        //then
        assertEquals(true, givenItem.isEquipped)
        assertEquals(givenAttrValue, player.addiAtkDmg)
    }

    @Test
    @DisplayName("선택된 장비가 장착되지 않은 상태에서 기존에 장착아이템이 있는 상황에 장착요청을 보낸 경우 기존 아이템은 해제, 선택아이템은 장착")
    fun changeEquipment() {
        //given
        val givenAttrValue = 5L
        val player = PlayerFixture.getPlayerBase()
            .apply {
                id = 1L
                addiAtkDmg = givenAttrValue
            }
        val equippedItem = PlayerItemFixture.getItemBase(player)
            .apply {
                id = 1L
                isEquipped = true
                finalAttrValue = givenAttrValue
                category = ItemCategory.WEAPON
            }
        val newAttrValue = 10L
        val selectedItem = PlayerItemFixture.getItemBase(player)
            .apply {
                id = 2L
                isEquipped = false
                finalAttrValue = newAttrValue
                category = ItemCategory.WEAPON
            }

        every { playerRepository.findByIdOrNull(any()) } returns player
        every { playerItemRepository.findByIdOrNull(any()) } returns selectedItem
        every { playerItemRepository.findByPlayerIdAndCategory(any(), any()) } returns equippedItem

        //when
        itemService.updateEquipped(player.id!!, selectedItem.id!!, 1L)

        //then
        assertEquals(false, equippedItem.isEquipped)
        assertEquals(true, selectedItem.isEquipped)
        assertEquals(newAttrValue, player.addiAtkDmg)
    }
}