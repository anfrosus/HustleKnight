package com.hustleknight.app.repository

import com.hustleknight.app.model.RaidMonster
import jakarta.persistence.LockModeType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Lock

interface RaidMonsterRepository: JpaRepository<RaidMonster, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    fun findWithLockById(id: Long): RaidMonster?
}