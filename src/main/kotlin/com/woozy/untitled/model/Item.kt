package com.woozy.untitled.model

import com.woozy.untitled.model.enums.ItemCategoryEnum
import com.woozy.untitled.model.enums.ItemTypeEnum
import jakarta.persistence.*

@Entity
@Table(name= "ITEM")
class Item(
    @Column(name = "ITEM_NAME")
    var name: String,

    @Column(name = "ITEM_ATTR_NAME")
    var attrName: String,

    @Column(name = "ITEM_ATTR_VALUE")
    var attrValue: Long,

    @Column(name = "ITEM_TYPE")
    @Enumerated(EnumType.STRING)
    var type: ItemTypeEnum,

    @Column(name = "ITEM_NAME")
    @Enumerated(EnumType.STRING)
    var category: ItemCategoryEnum

    ) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
}