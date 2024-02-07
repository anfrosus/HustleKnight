package com.woozy.untitled.repository

import com.woozy.untitled.model.GoodsDropTable
import org.springframework.data.jpa.repository.JpaRepository

interface GoodsDropTableRepository: JpaRepository<GoodsDropTable, Long> {

    fun findByMonsterId(monsterId: Long): GoodsDropTable
}