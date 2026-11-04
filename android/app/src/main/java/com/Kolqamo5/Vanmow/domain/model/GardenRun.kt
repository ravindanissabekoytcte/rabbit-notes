package com.Kolqamo5.Vanmow.domain.model

data class GardenRun(
    val board: List<List<Patch>>,
    val activeRow: Int = 1,
    val clearedRows: Int = 0,
    val carrots: Int = 0,
    val charms: Int = 0
) {
    val rows: Int get() = board.size

    val multiplier: Float get() = 1f + clearedRows * 0.3f

    val isFullBasket: Boolean get() = clearedRows >= rows

    fun rowAt(row: Int): List<Patch> = board[row - 1]
}
