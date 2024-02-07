package com.woozy.untitled.model

import com.woozy.untitled.exception.CustomException
import com.woozy.untitled.exception.ErrorCode
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

    fun decrease(long: Long){
        if (amount < long) {
            throw CustomException(ErrorCode.PLAYER_GOODS_NOT_ENOUGH)
        }
        amount -= long
    }
}