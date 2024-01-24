package com.woozy.untitled.repository

import com.woozy.untitled.model.Monster
import org.springframework.data.jpa.repository.JpaRepository

interface MonsterRepository : JpaRepository<Monster, Long> {
}