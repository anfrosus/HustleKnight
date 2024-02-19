package com.hustleknight.app.model

import jakarta.persistence.*
import kotlin.random.Random

@Entity
@Table(name = "GOODS_DROP_TABLE")
class GoodsDropTable(
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MONSTER_ID")
    var monster: Monster,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GOODS_ID")
    var goods: Goods,

    @Column(name = "GOODS_AMOUNT")
    var amount: Long,

    @Column(name = "GOODS_DROP_RATE")
    var dropRate: Double
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    init {
        monster.addGoodsDropTable(this)
    }

    fun isDropped(): Boolean {
        return Random.nextDouble() < dropRate
    }
}