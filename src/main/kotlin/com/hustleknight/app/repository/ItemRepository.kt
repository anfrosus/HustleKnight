package com.hustleknight.app.repository

import com.hustleknight.app.model.Item
import org.springframework.data.jpa.repository.JpaRepository

interface ItemRepository : JpaRepository<Item, Long> {

}