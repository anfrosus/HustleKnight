package com.woozy.untitled.repository

import com.woozy.untitled.model.Goods
import org.springframework.data.jpa.repository.JpaRepository

interface GoodsRepository : JpaRepository<Goods, Long> {

}
