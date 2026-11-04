package com.Kolqamo5.Vanmow.domain.usecase

import com.Kolqamo5.Vanmow.domain.model.GardenRun
import com.Kolqamo5.Vanmow.domain.model.PatchKind

data class RevealResult(
    val run: GardenRun,
    val kind: PatchKind,
    val gainedCarrots: Int,
    val caught: Boolean,
    val fullBasket: Boolean
)

class RevealPatchUseCase(
    private val baseCarrots: Int,
    private val rowCarrotStep: Int
) {

    operator fun invoke(run: GardenRun, column: Int): RevealResult {
        val row = run.activeRow
        val patches = run.rowAt(row)
        val target = patches[column]
        val revealedRow = patches.map { patch ->
            if (patch.column == column || target.kind == PatchKind.FOX) {
                patch.copy(revealed = true)
            } else {
                patch
            }
        }
        val board = run.board.toMutableList()
        board[row - 1] = revealedRow

        if (target.kind == PatchKind.FOX) {
            return RevealResult(
                run = run.copy(board = board),
                kind = PatchKind.FOX,
                gainedCarrots = 0,
                caught = true,
                fullBasket = false
            )
        }

        val gained = baseCarrots + row * rowCarrotStep
        val updated = run.copy(
            board = board,
            activeRow = row + 1,
            clearedRows = row,
            carrots = run.carrots + gained,
            charms = run.charms + if (target.kind == PatchKind.CHARM) 1 else 0
        )
        return RevealResult(
            run = updated,
            kind = target.kind,
            gainedCarrots = gained,
            caught = false,
            fullBasket = updated.isFullBasket
        )
    }
}
