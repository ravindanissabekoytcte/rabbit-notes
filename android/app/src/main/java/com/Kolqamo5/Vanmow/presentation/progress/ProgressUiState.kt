package com.Kolqamo5.Vanmow.presentation.progress

data class ProgressUiState(
    val level: Int = 1,
    val carrotsIntoLevel: Int = 0,
    val carrotsPerLevel: Int = 200,
    val runs: Int = 0,
    val bestRow: Int = 0,
    val carrots: Int = 0,
    val charms: Int = 0,
    val runHistory: List<Int> = emptyList(),
    val rowsPerRun: Int = 6
) {
    val levelFraction: Float
        get() = if (carrotsPerLevel <= 0) 0f else carrotsIntoLevel.toFloat() / carrotsPerLevel
}
