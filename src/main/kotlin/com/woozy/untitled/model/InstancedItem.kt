package com.woozy.untitled.model

import com.woozy.untitled.model.enums.ItemCategoryEnum
import com.woozy.untitled.model.enums.ItemTypeEnum
import jakarta.persistence.*

@Entity
@Table(name = "INSTANCED_ITEM")
class InstancedItem(

    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "PLAYER_ID")
    var player: Player,

    @Column(name = "ITEM_NAME")
    var name: String,

    @Column(name = "ITEM_ATTR_NAME")
    var attrName: String,

    @Column(name = "ITEM_ATTR_VALUE")
    var attrValue: Long,

    @Column(name = "ITEM_TYPE")
    @Enumerated(EnumType.STRING)
    var type: ItemTypeEnum,

    @Column(name = "ITEM_CATEGORY")
    @Enumerated(EnumType.STRING)
    var category: ItemCategoryEnum,

    //equip only
    @Column(name = "ITEM_SUCCESS_CNT", nullable = true)
    var successCnt: Int,

    @Column(name = "ITEM_REMAINING_CNT", nullable = true)
    var remainingCnt: Int,

    @Column(name = "INSTANCED_ITEM_ATTR_INCREASE", nullable = true)
    var attrIncrease: Int

) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
}