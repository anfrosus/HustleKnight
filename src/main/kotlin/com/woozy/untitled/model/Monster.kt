package com.woozy.untitled.model

import com.woozy.untitled.model.enums.MonsterType
import jakarta.persistence.*

@Entity
@Table(name = "MONSTER")
class Monster(

    @Column(name = "MONSTER_TYPE")
    @Enumerated(EnumType.STRING)
    var type: MonsterType,

    @Column(name = "MONSTER_STAGE")
    var stage: Long,

    @Column(name = "MONSTER_NAME")
    var name: String,

    @Column(name = "MONSTER_LEVEL")
    var level: Long,

    @Column(name = "MONSTER_AD")
    var atkDmg: Long,

    @Column(name = "MONSTER_HP")
    var hitPnt: Long,

    @Column(name = "MONSTER_DROP_EXP")
    var expReward: Long,

    @Column(name = "MONSTER_DROP_GOLD")
    var goldReward: Long,

    @OneToMany(mappedBy = "monster", fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    var itemDropTable: MutableList<ItemDropTable> = mutableListOf(),

    @OneToOne(mappedBy = "monster" , fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    var goodsDropTable: GoodsDropTable
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    fun addItemDropTable(itemDropTable: ItemDropTable){
        this.itemDropTable.add(itemDropTable)
    }

    fun addGoodsDropTable(goodsDropTable: GoodsDropTable){
        this.goodsDropTable = goodsDropTable
    }

}