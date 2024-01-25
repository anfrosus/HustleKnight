package com.woozy.untitled.model

import com.woozy.untitled.model.enums.ItemCategoryEnum
import com.woozy.untitled.model.enums.ItemTypeEnum
import jakarta.persistence.*

@Entity
@Table(name = "INSTANCED_ITEM")
class InstancedItem(

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PLAYER_ID")
    var player: Player,

    @Column(name = "ITEM_NAME")
    var name: String,

    @Column(name = "ITEM_ATTR_NAME")
    var attrName: String,

    @Column(name = "ITEM_ATTR_VALUE")
    var finalAttrValue: Long,

    @Column(name = "ITEM_TYPE")
    @Enumerated(EnumType.STRING)
    var type: ItemTypeEnum,

    @Column(name = "ITEM_CATEGORY")
    @Enumerated(EnumType.STRING)
    var category: ItemCategoryEnum,

    //equip only
    @Column(name = "ITEM_SUCCESS_CNT", nullable = true)
    var successCnt: Int = 0,

    @Column(name = "ITEM_REMAINING_CNT", nullable = true)
    var remainingCnt: Int = 0,

//    @Column(name = "INSTANCED_ITEM_ATTR_INCREASE", nullable = true)
//    var attrIncrease: Int = 0

) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null


    companion object {

        fun create(player: Player, item: Item): InstancedItem {
            return InstancedItem(
                player,
                item.name,
                item.attrName,
                item.attrValue,
                item.type,
                item.category,
                remainingCnt = 7
            )
        }
    }
}