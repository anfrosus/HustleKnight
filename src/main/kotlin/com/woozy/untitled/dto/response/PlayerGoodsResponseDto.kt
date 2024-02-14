package com.woozy.untitled.dto.response

import com.woozy.untitled.model.GoodsDropTable
import com.woozy.untitled.model.PlayerGoods
import com.woozy.untitled.model.enums.GoodsCategory

data class PlayerGoodsResponseDto(
    val goodsCategory: GoodsCategory,
    val amount: Long
) {
    companion object {
        fun fromEntity(playerGoods: PlayerGoods): PlayerGoodsResponseDto {
             return PlayerGoodsResponseDto(
                 goodsCategory = playerGoods.category,
                 amount = playerGoods.amount
             )
        }

        fun fromGoodsDropTable(goodsDropTable: GoodsDropTable): PlayerGoodsResponseDto {
            return PlayerGoodsResponseDto(
                goodsCategory = goodsDropTable.goods.category,
                amount = goodsDropTable.amount
            )
        }
    }

}
