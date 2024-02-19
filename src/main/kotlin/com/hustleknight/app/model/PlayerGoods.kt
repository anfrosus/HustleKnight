package com.hustleknight.app.model

import com.hustleknight.app.exception.CustomException
import com.hustleknight.app.exception.ErrorCode
import com.hustleknight.app.model.enums.GoodsCategory
import com.hustleknight.app.model.enums.ItemUpgradeCategory
import jakarta.persistence.*

@Entity
@Table(name = "PLAYER_GOODS")
class PlayerGoods(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PLAYER_ID")
    var player: Player,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GOODS_ID")
    var goods: Goods,

    @Enumerated(EnumType.STRING)
    @Column(name = "PLAYER_GOODS_CATEGORY")
    var category: GoodsCategory,

    @Column(name = "PLAYER_GOODS_AMOUNT")
    var amount: Long

) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    init {
        player.addPlayerGoods(this)
    }

    fun increase(long: Long){
        amount += long
    }

    private fun decrease(long: Long){
        if (amount < long) {
            throw CustomException(ErrorCode.PLAYER_GOODS_NOT_ENOUGH)
        }
        amount -= long
    }

    fun consume(reqLevel: Long, upgradeCategory: ItemUpgradeCategory) {

        val consumeStoneAmount = upgradeCategory.getRequireStoneAmount(reqLevel)
        val consumeGoldAmount = upgradeCategory.getRequireGoldAmount(reqLevel)

        if (this.amount < consumeStoneAmount || player.gold < consumeGoldAmount) {
            throw CustomException(
                ErrorCode.PLAYER_GOODS_NOT_ENOUGH,
                "필요 강화석: $consumeStoneAmount, 필요 골드: $consumeGoldAmount"
            )
        }
        this.decrease(consumeStoneAmount)
        player.gold -= consumeGoldAmount
    }
}