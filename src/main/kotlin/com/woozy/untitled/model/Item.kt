package com.woozy.untitled.model

import com.woozy.untitled.model.enums.ItemCategory
import com.woozy.untitled.model.enums.ItemType
import jakarta.persistence.*

@Entity
@Table(name= "ITEM")
class Item(
    @Column(name = "ITEM_NAME")
    var name: String,

    @Column(name = "ITEM_REQ_LVL")
    val reqLevel: Long,

    @Column(name = "ITEM_ATTR_NAME")
    var attrName: String,

    @Column(name = "ITEM_ATTR_VALUE")
    var attrValue: Long,

    @Column(name = "ITEM_TYPE")
    @Enumerated(EnumType.STRING)
    var type: ItemType,

    @Column(name = "ITEM_CATEGORY")
    @Enumerated(EnumType.STRING)
    var category: ItemCategory

    ) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
}