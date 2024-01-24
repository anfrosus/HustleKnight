package com.woozy.untitled.repository

import com.woozy.untitled.model.Equipped
import org.springframework.data.jpa.repository.JpaRepository

interface EquippedRepository: JpaRepository<Equipped, Long> {
}