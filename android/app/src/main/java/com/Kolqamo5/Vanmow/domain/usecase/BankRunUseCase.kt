package com.Kolqamo5.Vanmow.domain.usecase

import com.Kolqamo5.Vanmow.domain.model.GardenRun
import com.Kolqamo5.Vanmow.domain.model.RunOutcome
import kotlin.math.roundToInt

class BankRunUseCase(
    private val fullBasketBonus: Int
) {

    operator fun invoke(run: GardenRun, previousBestRow: Int): RunOutcome {
        val base = (run.carrots * run.multiplier).roundToInt()
        val carrots = if (run.isFullBasket) base * fullBasketBonus else base
        return RunOutcome(
            carrots = carrots,
            charms = run.charms,
            clearedRows = run.clearedRows,
            isFullBasket = run.isFullBasket,
            isNewBest = run.clearedRows > previousBestRow
        )
    }

    fun caught(run: GardenRun, previousBestRow: Int): RunOutcome = RunOutcome(
        carrots = 0,
        charms = run.charms,
        clearedRows = run.clearedRows,
        isFullBasket = false,
        isNewBest = run.clearedRows > previousBestRow
    )
}
