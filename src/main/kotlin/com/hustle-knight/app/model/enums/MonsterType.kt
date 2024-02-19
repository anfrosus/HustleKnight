package com.woozy.untitled.model.enums

import kotlin.random.Random

enum class MonsterType(private val rate: Double) {
    NORMAL(0.95), MUTANT(0.05), BOSS(0.0);
    companion object {
        fun randomType(isBoss: Boolean): MonsterType {
            return if (isBoss) {
                BOSS
            } else if (Random.nextDouble() < MUTANT.rate) {
                MUTANT
            } else {
                NORMAL
            }
        }
    }
}
