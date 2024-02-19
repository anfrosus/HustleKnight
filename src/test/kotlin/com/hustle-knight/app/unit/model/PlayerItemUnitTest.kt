package com.woozy.untitled.unit.model

import com.woozy.untitled.exception.CustomException
import com.woozy.untitled.exception.ErrorCode
import com.woozy.untitled.model.enums.ItemCategory
import com.woozy.untitled.model.enums.ItemUpgradeCategory
import com.woozy.untitled.unit.fixture.PlayerFixture
import com.woozy.untitled.unit.fixture.PlayerItemFixture
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class PlayerItemUnitTest {

    @Test
    @DisplayName("장비를 장착한 채로 강화가능여부 확인하면 예외를 던짐")
    fun checkEquippedBeforeUpgrade() {
        //given
        val player = PlayerFixture.getPlayerBase()
        val selectedItem = PlayerItemFixture.getItemBase(player)
            .apply { isEquipped = true }

        //when
        val e = assertThrows<CustomException> {
            selectedItem.checkUpgradable(selectedItem.category)
        }

        //then
        assertEquals(ErrorCode.ITEM_EQUIPPED, e.errorCode)
    }

    @Test
    @DisplayName("선택된 장비와 다른 종류의 강화 시도일 때 강화가능여부 확인하면 예외를 던짐")
    fun checkCategoryBeforeUpgrade() {
        //given
        val player = PlayerFixture.getPlayerBase()
        val selectedItem = PlayerItemFixture.getItemBase(player)
            .apply {
                category = ItemCategory.WEAPON
            }
        val givenCategory = ItemCategory.ARMOR

        //when
        val e = assertThrows<CustomException> {
            selectedItem.checkUpgradable(givenCategory)
        }

        //then
        assertEquals(ErrorCode.ITEM_CATEGORY_NOT_MATCH, e.errorCode)

    }

    @Test
    @DisplayName("강화횟수가 남지 않은 아이템의 강화가능여부를 확인하면 예외를 던짐")
    fun checkRemainingCountBeforeUpgrade() {
        //given
        val player = PlayerFixture.getPlayerBase()
        val selectedItem = PlayerItemFixture.getItemBase(player)
            .apply { remainingCnt = 0 }

        //when
        val e = assertThrows<CustomException> {
            selectedItem.checkUpgradable(selectedItem.category)
        }

        //then
        assertEquals(ErrorCode.ITEM_ZERO_REMAINING, e.errorCode)
    }

    @Test
    @DisplayName("강화 성공 시 성공횟수, 강화수치, 남은횟수가 정상적으로 반영되어야 함")
    fun upgrade() {
        //given
        val player = PlayerFixture.getPlayerBase()
        val givenCategory = ItemCategory.WEAPON
        val selectedUpgrade = ItemUpgradeCategory.WEAPON100
        val givenAttrValue = 10L
        val givenAttemptCnt = 5

        val selectedItem = PlayerItemFixture.getItemBase(player)
            .apply {
                category = givenCategory
                successCnt = 0
                remainingCnt = category.upgradeableCnt
                finalAttrValue = givenAttrValue
            }

        //when
        for (i in 1..givenAttemptCnt) {
            selectedItem.upgrade(selectedUpgrade)
        }

        //then
        assertEquals(givenAttemptCnt, selectedItem.successCnt)
        assertEquals(givenCategory.upgradeableCnt - givenAttemptCnt, selectedItem.remainingCnt)
        assertEquals(givenAttrValue + selectedUpgrade.increment * givenAttemptCnt, selectedItem.finalAttrValue)
    }

    @Test
    @DisplayName("장비 분해시 획득하는 강화석의 양")
    fun disassemble(){
        //given
        val player = PlayerFixture.getPlayerBase()
        val selectedItem = PlayerItemFixture.getItemBase(player)
            .apply {
                reqLevel = 5
                successCnt = 7
            }

        //when
        val stone = selectedItem.getStoneWhenDisassemble()

        //then
        assertEquals(12L, stone)
    }

}