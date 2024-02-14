package com.woozy.untitled.model.enums

enum class MaxValues(val value: Long) {
    MAX_LEVEL(40),
    MAX_TURN(20),
    MAX_AS(60),
    MAX_STAGE(30),
    MAX_GOLD(999_999_999),
    MAX_CONNECTION_MS(5 * 60 * 1000),
    MAX_BATTLE_SAVED_MINUTE(5)
    ;
}