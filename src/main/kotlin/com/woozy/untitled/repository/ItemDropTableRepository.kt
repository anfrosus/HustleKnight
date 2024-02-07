package com.woozy.untitled.repository

import com.woozy.untitled.model.ItemDropTable
import org.springframework.data.jpa.repository.JpaRepository

interface ItemDropTableRepository : JpaRepository<ItemDropTable, Long> {
    fun findByMonsterId(monsterId: Long): List<ItemDropTable>
}