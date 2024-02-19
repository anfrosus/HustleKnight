package com.woozy.untitled.model

import com.woozy.untitled.exception.CustomException
import com.woozy.untitled.exception.ErrorCode
import com.woozy.untitled.model.enums.*
import jakarta.persistence.*

@Entity
@Table(name = "PLAYER", indexes = [Index(name = "IDX_PLAYER_RAID_TIER", columnList = "PLAYER_RAID_TIER")])
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
    var level: Int = 1,

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

    @Column(name = "PLAYER_TICKET")
    var ticket: Int = 3,

    @Column(name = "PLAYER_RAID_SCORE")
    var raidScore: Long = 0,

    @Column(name = "PLAYER_RAID_RANK", nullable = true)
    var raidRank: Long? = null,

    @Column(name = "PLAYER_RAID_TIER")
    @Enumerated(EnumType.STRING)
    var raidTier: RaidTier = RaidTier.UNRANKED,

    @OneToMany(mappedBy = "player", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    var playerGoodsList: MutableList<PlayerGoods> = mutableListOf(),

    ) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    fun earnRewards(monster: Monster) {
        exp += monster.expReward
        levelUp()
        gold += monster.goldReward
    }

    fun goNextStageIfBoss(monster: Monster) {
        if (monster.stage > maxStage) {
            throw CustomException(ErrorCode.PLAYER_ILLEGAL_STAGE)
        } else if (monster.stage == maxStage && monster.type == MonsterType.BOSS) {
            maxStage++
            curStage = maxStage
        }
    }

    fun addPlayerGoods(playerGoods: PlayerGoods) {
        playerGoodsList.add(playerGoods)
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
        playerItem.equip()
    }

    fun takeOff(playerItem: PlayerItem) {
        if (!playerItem.isEquipped) {
            throw CustomException(ErrorCode.ITEM_NOT_EQUIPPED)
        }
        when (playerItem.category) {
            ItemCategory.WEAPON -> {
                addiAtkDmg -= playerItem.finalAttrValue
            }

            ItemCategory.ACCESSORY -> {
                addiAtkSpd -= playerItem.finalAttrValue
            }

            ItemCategory.ARMOR -> {
                addiHitPnt -= playerItem.finalAttrValue
            }
        }
        playerItem.unEquip()
    }

    private fun levelUp() {
        var requiredExp = this.requiredExp()
        while (exp >= requiredExp && level < MaxValues.MAX_LEVEL.value) {
            exp -= requiredExp
            level++
            atkDmg += 1
            atkSpd += 1
            hitPnt += 5
            requiredExp = this.requiredExp()
        }
    }

    fun checkStageAvailable(selectedStage: Long) {
        if (this.maxStage < selectedStage || 0 >= selectedStage) {
            throw CustomException(ErrorCode.PLAYER_ILLEGAL_STAGE)
        }
        curStage = selectedStage
    }

    fun requiredExp(): Long {
        return (level * level * 50).toLong()
    }

    fun earnGoods(goodsDropTable: GoodsDropTable) {
        if (goodsDropTable.isDropped()) {
            this.playerGoodsList.first {
                it.category == goodsDropTable.goods.category
            }.increase(goodsDropTable.amount)
        }
    }

    fun consumeTicket() {
        if (this.ticket <= 0) {
            throw CustomException(ErrorCode.PLAYER_NOT_FOUND)
        }
        ticket -= 1
    }

    fun earnScore(score: Long) {
        this.raidScore += score
        this.raidTier = RaidTier.getTierByScore(score)
    }

    fun earnGold(gold: Long) {
        this.gold += gold
    }

    fun ranking(rank: Long) {
        this.raidRank = rank
    }

    fun resetScore() {
        this.raidScore = 0
    }

    fun resetTicket() {
        ticket =
            when (this.role) {
                PlayerRole.PREMIUM -> { 5 }
                PlayerRole.REGULAR -> { 3 }
            }
    }

}