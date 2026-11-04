package com.Kolqamo5.Vanmow.presentation.menu

data class MenuUiState(
    val carrots: Int = 0,
    val bestRun: Int = 0,
    val charms: Int = 0
) {
    val showStats: Boolean get() = bestRun > 0 && charms > 0
}
