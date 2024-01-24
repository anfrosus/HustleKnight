package com.woozy.untitled.repository

import com.woozy.untitled.model.InstancedItem
import org.springframework.data.jpa.repository.JpaRepository

interface InstancedItemRepository: JpaRepository<InstancedItem, Long> {
}