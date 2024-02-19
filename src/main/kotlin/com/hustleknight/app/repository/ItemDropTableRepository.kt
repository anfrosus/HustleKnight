package com.hustleknight.app.repository

import com.hustleknight.app.model.ItemDropTable
import org.springframework.data.jpa.repository.JpaRepository

interface ItemDropTableRepository : JpaRepository<ItemDropTable, Long> {

}