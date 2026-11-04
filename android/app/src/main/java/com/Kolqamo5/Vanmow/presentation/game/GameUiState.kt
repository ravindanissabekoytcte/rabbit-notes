package com.Kolqamo5.Vanmow.presentation.game

import com.Kolqamo5.Vanmow.domain.model.Patch

enum class GamePhase {
    READY,
    HOPPING,
    RUNNING,
    CAUGHT,
    BANKED,
    FULL_BASKET
}

data class GameUiState(
    val board: List<List<Patch>> = emptyList(),
    val rows: Int = 0,
    val columns: Int = 0,
    val activeRow: Int = 1,
    val clearedRows: Int = 0,
    val runCarrots: Int = 0,
    val runCharms: Int = 0,
    val balance: Int = 0,
    val multiplier: Float = 1f,
    val phase: GamePhase = GamePhase.READY,
    val paused: Boolean = false,
    val shaking: Boolean = false
) {
    val canTap: Boolean
        get() = !paused && (phase == GamePhase.READY || phase == GamePhase.RUNNING)

    val canCollect: Boolean
        get() = !paused && phase == GamePhase.RUNNING && clearedRows > 0

    val basketFraction: Float
        get() = if (rows <= 0) 0f else clearedRows.toFloat() / rows
}
