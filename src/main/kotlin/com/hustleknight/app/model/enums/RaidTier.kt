package com.hustleknight.app.model.enums

enum class RaidTier(val requiredScore: Long) {
    UNRANKED(0),
    BRONZE(10),
    SILVER(100),
    GOLD(1_000),
    PLATINUM(10_000),
    DIAMOND(100_000),
    MASTER(1_000_000);

    companion object{
        fun getTierByScore(raidScore: Long): RaidTier{
            return when {
                raidScore >= MASTER.requiredScore -> {
                    MASTER
                }
                raidScore >= DIAMOND.requiredScore -> {
                    DIAMOND
                }
                raidScore >= PLATINUM.requiredScore -> {
                    PLATINUM
                }
                raidScore >= GOLD.requiredScore -> {
                    GOLD
                }
                raidScore >= SILVER.requiredScore -> {
                    SILVER
                }
                raidScore >= BRONZE.requiredScore -> {
                    BRONZE
                }
                else -> {
                    UNRANKED
                }
            }
        }
    }
}