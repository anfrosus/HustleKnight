package com.woozy.untitled.model

import jakarta.persistence.*

@Entity
@Table(name = "DROP_TABLE")
class DropTable(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MONSTER_ID")
    var monster: Monster,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ITEM_ID")
    var item: Item,

    @Column(name = "DROP_RATE")
    var dropRate: Double
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    init {
        monster.addDropTable(this)
    }
}