package com.woozy.untitled.model

import jakarta.persistence.*

@Entity
@Table(name = "ITEM_DROP_TABLE")
class ItemDropTable(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MONSTER_ID")
    var monster: Monster,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ITEM_ID")
    var item: Item,

    @Column(name = "ITEM_DROP_RATE")
    var dropRate: Double
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    init {
        monster.addItemDropTable(this)
    }
}