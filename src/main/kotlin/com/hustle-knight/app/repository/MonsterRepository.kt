package com.woozy.untitled.repository

import com.woozy.untitled.model.Monster
import com.woozy.untitled.model.enums.MonsterType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface MonsterRepository : JpaRepository<Monster, Long> {

    @Query("select m from Monster m left join fetch m.itemDropTable where m.stage = :stage and m.type = :type")
    fun findByStageAndType(stage: Long, type: MonsterType): Monster?

    @Query("select m from Monster m left join fetch m.itemDropTable join fetch m.goodsDropTable")
    fun findByIdFetchDropTable(monsterId: Long): Monster?
}