package com.woozy.untitled.unit.model

import com.woozy.untitled.exception.CustomException
import com.woozy.untitled.exception.ErrorCode
import com.woozy.untitled.model.Goods
import com.woozy.untitled.model.GoodsDropTable
import com.woozy.untitled.model.PlayerGoods
import com.woozy.untitled.model.enums.GoodsCategory
import com.woozy.untitled.model.enums.ItemCategory
import com.woozy.untitled.model.enums.MaxValues
import com.woozy.untitled.model.enums.MonsterType
import com.woozy.untitled.unit.fixture.MonsterFixture
import com.woozy.untitled.unit.fixture.PlayerFixture
import com.woozy.untitled.unit.fixture.PlayerItemFixture
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class PlayerUnitTest {

    @Test
    @DisplayName("골드와 경험치 획득")
    fun earnReward() {
        //given
        val dropGold = 10L
        val dropExp = 10L
        val monster = MonsterFixture.getMonsterBase()
            .apply {
                goldReward = dropGold
                expReward = dropExp
            }
        val player = PlayerFixture.getPlayerBase()
            .apply {
                gold = 0
                exp = 0
            }

        //when
        player.earnRewards(monster)

        //then
        assertTrue(player.gold == 10L)
        assertTrue(player.exp == 10L)

    }

    @Test
    @DisplayName("경험치 획득 후 레벨업 요구 경험치를 충족 시키면 레벨업")
    fun levelUpAfterEarned() {
        //given
        val givenLevel = 2
        val player = PlayerFixture.getPlayerBase()
            .apply { level = givenLevel }
        val reqExp = player.requiredExp()
        val monster = MonsterFixture.getMonsterBase()
            .apply { expReward = reqExp }
        //when
        player.earnRewards(monster)

        //then
        assertTrue(player.level == givenLevel + 1)
        assertTrue(player.exp == 0L)
    }

    @Test
    @DisplayName("2단계의 레벨 이상의 경험치가 들어왔을 때의 레벨업")
    fun leveUpTwoTimes() {
        //given
        val givenLevel = 2
        val player = PlayerFixture.getPlayerBase()
            .apply { level = givenLevel }
        val reqExp = player.requiredExp()
        player.level = 3
        val reqExp2 = player.requiredExp()
        val dropExp = reqExp + reqExp2
        player.level = 2


        val monster = MonsterFixture.getMonsterBase()
            .apply { expReward = dropExp }

        //when
        player.earnRewards(monster)

        //then
        assertTrue(player.level == givenLevel + 2)
        assertTrue(player.exp == 0L)
    }

    @Test
    @DisplayName("제한 레벨 이상으로는 레벨업이 진행되지 않음")
    fun maxLevelUp() {
        //given
        val maxLevel = MaxValues.MAX_LEVEL.value.toInt()
        val player = PlayerFixture.getPlayerBase()
            .apply {
                level = maxLevel
                gold = 0
                atkDmg = 1
            }
        val reqExp = player.requiredExp()
        val expectedGold = 10L
        val monster = MonsterFixture.getMonsterBase()
            .apply {
                expReward = reqExp
                goldReward = expectedGold
            }

        //when
        player.earnRewards(monster)

        //when
        assertEquals(maxLevel,player.level)
        assertEquals(expectedGold,player.gold)
        assertEquals(1, player.atkDmg)

        //then
    }

    //분기 별 테스트
    @Test
    @DisplayName("보스 몬스터 처치시 다음 스테이지로")
    fun goNextStageIfBoss() {
        //given
        val bossMonster = MonsterFixture.getMonsterBase()
            .apply { type = MonsterType.BOSS }
        val player = PlayerFixture.getPlayerBase()
            .apply {
                curStage = 1
                maxStage = 1
            }

        //when
        player.goNextStageIfBoss(bossMonster)

        //then
        assertTrue(player.curStage == 2L)
        assertTrue(player.maxStage == 2L)
    }

    @Test
    @DisplayName("몬스터의 스테이지가 플레이어의 최고 스테이지 보다 높을 때 예외를 발생")
    fun throwWhenIllegalStage() {
        //given
        val playerMaxStage = 2L
        val monsterStage = 3L
        val monster = MonsterFixture.getMonsterBase()
            .apply { stage = monsterStage }
        val player = PlayerFixture.getPlayerBase()
            .apply { maxStage = playerMaxStage }

        //when
        val e = assertThrows<CustomException> {
            player.goNextStageIfBoss(monster)
        }
        //then
        assertEquals(e.errorCode, ErrorCode.PLAYER_ILLEGAL_STAGE)
    }

    @Test
    @DisplayName("최고 스테이지의 몬스터지만 보스가 아닐 때 스테이지가 바뀌지 않아야 함")
    fun doNotGoToTheNextStageIfNotBoss() {
        //given
        val givenStage = 1L
        val notBossMonster = MonsterFixture.getMonsterBase()
            .apply {
                type = MonsterType.NORMAL
                stage = givenStage
            }
        val player = PlayerFixture.getPlayerBase()
            .apply { maxStage = givenStage }

        //when
        player.goNextStageIfBoss(notBossMonster)

        //then
        assertTrue(player.curStage == givenStage)
        assertTrue(player.maxStage == givenStage)
    }

    @Test
    @DisplayName("장비 장착 시 요구 레벨이 부족하다면 예외를 던짐")
    fun tryToPutOnHighEquipment() {
        //given
        val playerLevel = 1
        val itemReqLevel = 5L
        val player = PlayerFixture.getPlayerBase()
            .apply { level = playerLevel }
        val playerItem = PlayerItemFixture.getItemBase(player)
            .apply { reqLevel = itemReqLevel }

        //when
        val e = assertThrows<CustomException> {
            player.putOn(playerItem)
        }

        //then
        assertEquals(e.errorCode, ErrorCode.PLAYER_LEVEL_NOT_MET)
    }

    @Test
    @DisplayName("장비를 입을 때 알맞게 능력치가 적용되고 착용상태로 바뀌어야 함")
    fun putOn() {
        //given
        val player = PlayerFixture.getPlayerBase()
        val givenAd = 10L
        val givenHp = 20L
        val givenAs = 5L

        val weapon = PlayerItemFixture.getItemBase(player)
            .apply {
                category = ItemCategory.WEAPON
                finalAttrValue = givenAd
            }
        val armor = PlayerItemFixture.getItemBase(player)
            .apply {
                category = ItemCategory.ARMOR
                finalAttrValue = givenHp
            }
        val accessory = PlayerItemFixture.getItemBase(player)
            .apply {
                category = ItemCategory.ACCESSORY
                finalAttrValue = givenAs
            }

        //when
        player.putOn(weapon)
        player.putOn(armor)
        player.putOn(accessory)

        //then
        assertTrue(player.addiAtkDmg == givenAd)
        assertTrue(player.addiHitPnt == givenHp)
        assertTrue(player.addiAtkSpd == givenAs)
        assertTrue(weapon.isEquipped)
        assertTrue(armor.isEquipped)
        assertTrue(accessory.isEquipped)
    }

    @Test
    @DisplayName("장비를 벗을 때 알맞게 능력치가 적용되고 미착용 상태로 바뀌어야 함")
    fun takeOff() {
        //given
        val player = PlayerFixture.getPlayerBase()
        val givenAd = 10L
        val givenHp = 20L
        val givenAs = 5L
        val weapon = PlayerItemFixture.getItemBase(player)
            .apply {
                category = ItemCategory.WEAPON
                finalAttrValue = givenAd
            }
        val armor = PlayerItemFixture.getItemBase(player)
            .apply {
                category = ItemCategory.ARMOR
                finalAttrValue = givenHp
            }
        val accessory = PlayerItemFixture.getItemBase(player)
            .apply {
                category = ItemCategory.ACCESSORY
                finalAttrValue = givenAs
            }
        player.putOn(weapon)
        player.putOn(armor)
        player.putOn(accessory)

        //when
        player.takeOff(weapon)
        player.takeOff(armor)
        player.takeOff(accessory)

        //then
        assertTrue(player.addiAtkDmg == 0L)
        assertTrue(player.addiHitPnt == 0L)
        assertTrue(player.addiAtkSpd == 0L)
        assertTrue(!weapon.isEquipped)
        assertTrue(!armor.isEquipped)
        assertTrue(!accessory.isEquipped)
    }

    @Test
    @DisplayName("재화가 드랍되었을 때 플레이어의 재화에 반영되어야함 + 드랍되지 않았을 때는 반영되지 않아야함")
    fun addPlayerGoods() {
        //given
        val player = PlayerFixture.getPlayerBase()
        val redStone = Goods(GoodsCategory.RED_STONE, "무기 강화")
        val blueStone = Goods(GoodsCategory.BLUE_STONE, "장신구 강화")
        val givenPlayerGoodsList = mutableListOf(
            PlayerGoods(player, redStone, redStone.category, 0),
            PlayerGoods(player, blueStone, blueStone.category, 0)
        )
        player.apply { playerGoodsList = givenPlayerGoodsList }
        val monster = MonsterFixture.getMonsterBase()
        val monster2 = MonsterFixture.getMonsterBase()
        val givenAmount = 10L
        val goodsDropTable = GoodsDropTable(monster, redStone, givenAmount, 1.0)
        val goodsDropTable2 = GoodsDropTable(monster2, blueStone, givenAmount, 0.0)



        //when
        player.earnGoods(goodsDropTable)
        player.earnGoods(goodsDropTable2)

        //then
        assertEquals(givenAmount, player.playerGoodsList[0].amount)
        assertEquals(0, player.playerGoodsList[1].amount)
    }

}