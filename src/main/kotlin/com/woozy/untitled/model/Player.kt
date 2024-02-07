package com.woozy.untitled.model

import com.woozy.untitled.dto.BattleDto
import com.woozy.untitled.model.enums.PlayerRole
import com.woozy.untitled.exception.CustomException
import com.woozy.untitled.exception.ErrorCode
import com.woozy.untitled.model.enums.ItemCategory
import com.woozy.untitled.model.enums.ItemUpgradeCategory
import com.woozy.untitled.model.enums.MonsterType
import jakarta.persistence.*

@Entity
@Table(name = "PLAYER")
class Player(
    @Column(name = "PLAYER_EMAIL")
    var email: String,

    @Column(name = "PLAYER_PASSWORD")
    var password: String,

    @Column(name = "PLAYER_NAME")
    var name: String,

    @Column(name = "PLAYER_ROLE")
    @Enumerated(value = EnumType.STRING)
    var role: PlayerRole,

    @Column(name = "PLAYER_LEVEL")
    var level: Long = 1L,

    @Column(name = "PLAYER_AD")
    var atkDmg: Long = 5L,

    // time per attack
    @Column(name = "PLAYER_AS")
    var atkSpd: Long = 5,

    @Column(name = "PLAYER_HP")
    var hitPnt: Long = 50L,

    @Column(name = "PLAYER_ADDI_AD")
    var addiAtkDmg: Long = 0L,

    @Column(name = "PLAYER_ADDI_AS")
    var addiAtkSpd: Long = 0L,

    @Column(name = "PLAYER_ADDI_HP")
    var addiHitPnt: Long = 0L,

    @Column(name = "PLAYER_MAX_STAGE")
    var maxStage: Long = 1L,

    @Column(name = "PLAYER_CUR_STAGE")
    var curStage: Long = 1L,

    @Column(name = "PLAYER_EXP")
    var exp: Long = 0L,

    @Column(name = "PLAYER_GOLD")
    var gold: Long = 0L,

    @OneToMany(mappedBy = "player", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    var playerGoodsList: MutableList<PlayerGoods> = mutableListOf()

) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    fun earnRewards(battleDto: BattleDto) {
        exp += battleDto.expectedExp
        levelUp()
        gold += battleDto.expectedGold
    }

//    fun goNextStageIfBoss(monster: Monster) {
//        if (monster.stage > maxStage) {
//            throw CustomException(ErrorCode.PLAYER_ILLEGAL_STAGE)
//        } else if (monster.stage == maxStage && monster.type == MonsterType.BOSS) {
//            maxStage++
//            curStage = maxStage
//        }
//    }

    fun goNextStageIfBoss(battleDto: BattleDto) {
        if (battleDto.stage > maxStage) {
            throw CustomException(ErrorCode.PLAYER_ILLEGAL_STAGE)
        } else if (battleDto.stage == maxStage && battleDto.monsterType == MonsterType.BOSS) {
            maxStage++
            curStage = maxStage
        }
    }

    fun addPlayerGoods(playerGoods: PlayerGoods) {
        playerGoodsList.add(playerGoods)
    }

    fun takeOff(playerItem: PlayerItem) {
        //TODO: 그럴일은 없겠지만 음수가 되는지 확인해야하나?
        when (playerItem.category) {
            ItemCategory.WEAPON -> {
                if (addiAtkDmg < playerItem.finalAttrValue) {
                    throw CustomException(ErrorCode.PLAYER_STATS_CAN_NOT_MINUS)
                }
                addiAtkDmg -= playerItem.finalAttrValue
            }

            ItemCategory.ACCESSORY -> {
                if (addiAtkSpd < playerItem.finalAttrValue){
                    throw CustomException(ErrorCode.PLAYER_STATS_CAN_NOT_MINUS)
                }
                    addiAtkSpd -= playerItem.finalAttrValue
            }

            ItemCategory.ARMOR -> {
                if (addiHitPnt < playerItem.finalAttrValue) {
                    throw CustomException(ErrorCode.PLAYER_STATS_CAN_NOT_MINUS)
                }
                addiHitPnt -= playerItem.finalAttrValue
            }
        }
        playerItem.isEquipped = false
    }

    fun putOn(playerItem: PlayerItem) {
        if (playerItem.reqLevel > level) {
            throw CustomException(ErrorCode.PLAYER_LEVEL_NOT_MET)
        }
        when (playerItem.category) {
            ItemCategory.WEAPON -> addiAtkDmg += playerItem.finalAttrValue
            ItemCategory.ACCESSORY -> addiAtkSpd += playerItem.finalAttrValue
            ItemCategory.ARMOR -> addiHitPnt += playerItem.finalAttrValue
        }
        playerItem.isEquipped = true
    }

    private fun levelUp() {
        val requiredExp = level * level * 50
        while (requiredExp < exp) {
            exp -= requiredExp
            level++
            atkDmg += 1
            atkSpd += 1
            hitPnt += 5
        }
    }

    fun checkStageAvailable(selectedStage: Long) {
        if (this.maxStage < selectedStage || 0 >= selectedStage) {
            throw CustomException(ErrorCode.PLAYER_ILLEGAL_STAGE)
        }
        curStage = selectedStage
    }

    fun consumeGoods(reqLevel: Long, upgradeCategory: ItemUpgradeCategory) {
        val playerGoods = playerGoodsList.filter {
            it.goods.category == upgradeCategory.itemCategory.reqStone
        }.first()

        val consumeStoneAmount = upgradeCategory.getRequireStoneAmount(reqLevel)
        val consumeGoldAmount = upgradeCategory.getRequireGoldAmount(reqLevel)

        if (playerGoods.amount - consumeStoneAmount < 0 || this.gold - consumeGoldAmount < 0) {
            throw CustomException(
                ErrorCode.PLAYER_GOODS_NOT_ENOUGH,
                "필요 강화석: ${consumeStoneAmount}, 필요 골드: ${consumeGoldAmount}"
            )
        }
        playerGoods.decrease(consumeStoneAmount)
        this.gold -= consumeGoldAmount
    }
}