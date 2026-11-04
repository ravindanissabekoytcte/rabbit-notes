package com.Kolqamo5.Vanmow.presentation.dailybonus

data class DailyBonusUiState(
    val ladderPosition: Int = 1,
    val ladderDays: Int = 7,
    val claimedDays: Int = 0,
    val carrots: Int = 0,
    val charms: Int = 0,
    val claimedToday: Boolean = false,
    val balance: Int = 0
)
