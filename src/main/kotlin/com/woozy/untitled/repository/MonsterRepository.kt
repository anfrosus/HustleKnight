package com.woozy.untitled.repository

import com.woozy.untitled.model.Monster
import com.woozy.untitled.model.enums.MonsterTypeEnum
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface MonsterRepository : JpaRepository<Monster, Long> {

    @Query("select m from Monster m where m.stage = :stage and m.type = :type")
    fun findByStageAndType(stage: Long, type: MonsterTypeEnum): Monster?
}