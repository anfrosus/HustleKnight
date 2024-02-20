package com.hustleknight.app.model

import com.hustleknight.app.model.enums.ItemCategory
import jakarta.persistence.*

@Entity
@Table(name= "ITEM")
class Item(

    @Column(name = "ITEM_CATEGORY")
    @Enumerated(EnumType.STRING)
    var category: ItemCategory,

    @Column(name = "ITEM_NAME")
    var name: String,

    @Column(name = "ITEM_REQ_LVL")
    var reqLevel: Long,

    @Column(name = "ITEM_ATTR_NAME")
    var attrName: String,

    @Column(name = "ITEM_ATTR_VALUE")
    var attrValue: Long,

    @Column(name = "ITEM_DESCRIPTION")
    var description: String

//    @Column(name = "ITEM_TYPE")
//    @Enumerated(EnumType.STRING)
//    var type: ItemType,

    ) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
}