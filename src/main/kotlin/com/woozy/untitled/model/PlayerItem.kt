package com.woozy.untitled.model

import com.woozy.untitled.exception.CustomException
import com.woozy.untitled.exception.ErrorCode
import com.woozy.untitled.model.enums.ItemCategory
import com.woozy.untitled.model.enums.ItemUpgradeCategory
import jakarta.persistence.*

@Entity
@Table(name = "PLAYER_ITEM")
class PlayerItem(

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PLAYER_ID")
    var player: Player,

//    @Column(name = "ITEM_TYPE")
//    @Enumerated(EnumType.STRING)
//    var type: ItemType,

    @Column(name = "ITEM_CATEGORY")
    @Enumerated(EnumType.STRING)
    var category: ItemCategory,

    @Column(name = "ITEM_NAME")
    var name: String,

    @Column(name = "ITEM_REQ_LVL")
    var reqLevel: Long,

    @Column(name = "ITEM_ATTR_NAME")
    var attrName: String = category.attrName,

    @Column(name = "ITEM_ATTR_VALUE")
    var finalAttrValue: Long,

    @Column(name = "ITEM_SUCCESS_CNT")
    var successCnt: Int = 0,

    @Column(name = "ITEM_REMAINING_CNT")
    var remainingCnt: Int = category.upgradeableCnt,

    @Column(name = "ITEM_IS_EQUIPPED")
    var isEquipped: Boolean = false

) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null
    fun checkUpgradable(itemCategory: ItemCategory) {
        //장비를 벗고 강화해라
        this.checkUnequipped()
        //올바른 장비 종류에 대한 강화 요청인지
        if (this.category != itemCategory) {
            throw CustomException(ErrorCode.ITEM_CATEGORY_NOT_MATCH)
        }
        //강화 횟수 남았는지
        if (this.remainingCnt < 1) {
            throw CustomException(ErrorCode.ITEM_ZERO_REMAINING)
        }
    }
    fun checkUnequipped(){
        if(this.isEquipped){
            throw CustomException(ErrorCode.ITEM_EQUIPPED)
        }
    }
    fun upgrade(upgradeCategory: ItemUpgradeCategory) {
        if (upgradeCategory.isSuccess()) {
            this.successCnt++
            this.finalAttrValue += upgradeCategory.increment
        }
        this.remainingCnt--
    }

    fun getStoneWhenDisassemble(): Long{
        return reqLevel + (successCnt * 1)
    }

    fun equip() {
        this.isEquipped = true
    }

    fun unEquip(){
        this.isEquipped = false
    }
}