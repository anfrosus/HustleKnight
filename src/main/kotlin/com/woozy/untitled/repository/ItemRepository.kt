package com.woozy.untitled.repository

import com.woozy.untitled.model.Item
import org.springframework.data.jpa.repository.JpaRepository

interface ItemRepository : JpaRepository<Item, Long> {

}