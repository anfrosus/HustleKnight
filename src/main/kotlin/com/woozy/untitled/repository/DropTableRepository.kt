package com.woozy.untitled.repository

import com.woozy.untitled.model.DropTable
import org.springframework.data.jpa.repository.JpaRepository

interface DropTableRepository : JpaRepository<DropTable, Long> {
}