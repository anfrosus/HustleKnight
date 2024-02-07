package com.woozy.untitled.model

import com.woozy.untitled.model.enums.GoodsCategory
import com.woozy.untitled.model.enums.ItemCategory
import com.woozy.untitled.model.enums.ItemType
import jakarta.persistence.*

@Entity
@Table(name = "GOODS")
class Goods(
    @Column(name = "GOODS_NAME")
    var name: String,

    @Column(name = "GOODS_CATEGORY")
    @Enumerated(EnumType.STRING)
    var category: GoodsCategory,

    @Column(name = "GOODS_DESCRIPTION")
    var description: String
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
}