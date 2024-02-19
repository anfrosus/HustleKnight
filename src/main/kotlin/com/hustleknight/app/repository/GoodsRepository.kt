package com.hustleknight.app.repository

import com.hustleknight.app.model.Goods
import org.springframework.data.jpa.repository.JpaRepository

interface GoodsRepository : JpaRepository<Goods, Long> {

}
