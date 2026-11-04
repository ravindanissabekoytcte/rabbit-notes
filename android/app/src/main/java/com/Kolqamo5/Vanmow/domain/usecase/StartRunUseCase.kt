package com.Kolqamo5.Vanmow.domain.usecase

import com.Kolqamo5.Vanmow.domain.model.GardenRun
import com.Kolqamo5.Vanmow.domain.model.Patch
import com.Kolqamo5.Vanmow.domain.model.PatchKind
import kotlin.random.Random

class StartRunUseCase(
    private val rows: Int,
    private val columns: Int,
    private val charmRows: Set<Int>,
    private val random: Random = Random.Default
) {

    operator fun invoke(): GardenRun {
        val board = (1..rows).map { row ->
            val foxColumn = random.nextInt(columns)
            val safeColumns = (0 until columns).filter { it != foxColumn }
            val charmColumn = if (charmRows.contains(row)) {
                safeColumns[random.nextInt(safeColumns.size)]
            } else {
                -1
            }
            (0 until columns).map { column ->
                val kind = when (column) {
                    foxColumn -> PatchKind.FOX
                    charmColumn -> PatchKind.CHARM
                    else -> PatchKind.CARROT
                }
                Patch(row = row, column = column, kind = kind)
            }
        }
        return GardenRun(board = board)
    }
}
