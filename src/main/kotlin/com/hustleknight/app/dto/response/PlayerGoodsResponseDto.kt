package com.hustleknight.app.dto.response

import com.hustleknight.app.model.GoodsDropTable
import com.hustleknight.app.model.PlayerGoods
import com.hustleknight.app.model.enums.GoodsCategory

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
