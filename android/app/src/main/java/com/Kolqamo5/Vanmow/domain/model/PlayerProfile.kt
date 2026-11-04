package com.Kolqamo5.Vanmow.domain.model

data class PlayerProfile(
    val carrots: Int = 0,
    val totalCarrots: Int = 0,
    val charms: Int = 0,
    val runs: Int = 0,
    val fullBaskets: Int = 0,
    val bestRow: Int = 0,
    val ownedCharms: Set<Int> = emptySet(),
    val claimedTasks: Set<Int> = emptySet(),
    val runHistory: List<Int> = emptyList(),
    val lastClaimDay: Long = 0L,
    val dailyStreak: Int = 0,
    val soundOn: Boolean = true,
    val hapticsOn: Boolean = true,
    val reducedMotion: Boolean = false
) {
    val level: Int get() = 1 + totalCarrots / 200

    val carrotsIntoLevel: Int get() = totalCarrots % 200

    val carrotsPerLevel: Int get() = 200
}
