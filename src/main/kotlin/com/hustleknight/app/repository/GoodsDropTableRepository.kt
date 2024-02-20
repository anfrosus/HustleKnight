package com.hustleknight.app.repository

import com.hustleknight.app.model.GoodsDropTable
import org.springframework.data.jpa.repository.JpaRepository

interface GoodsDropTableRepository : JpaRepository<GoodsDropTable, Long> {

}