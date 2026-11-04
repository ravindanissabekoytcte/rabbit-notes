package com.Kolqamo5.Vanmow.core.di

import android.content.Context
import com.Kolqamo5.Vanmow.core.config.GameConfig
import com.Kolqamo5.Vanmow.data.local.GardenPreferences
import com.Kolqamo5.Vanmow.data.repository.GardenRepositoryImpl
import com.Kolqamo5.Vanmow.data.sample.SampleData
import com.Kolqamo5.Vanmow.domain.repository.GardenRepository
import com.Kolqamo5.Vanmow.domain.usecase.BankRunUseCase
import com.Kolqamo5.Vanmow.domain.usecase.ClaimDailyBonusUseCase
import com.Kolqamo5.Vanmow.domain.usecase.RevealPatchUseCase
import com.Kolqamo5.Vanmow.domain.usecase.RewardProgressUseCase
import com.Kolqamo5.Vanmow.domain.usecase.StartRunUseCase

object ServiceLocator {

    private var gardenRepository: GardenRepository? = null
    private var factory: AppViewModelFactory? = null

    val startRun = StartRunUseCase(
        rows = GameConfig.ROWS,
        columns = GameConfig.COLS,
        charmRows = setOf(GameConfig.CHARM_ROW_FIRST, GameConfig.CHARM_ROW_SECOND)
    )

    val revealPatch = RevealPatchUseCase(
        baseCarrots = GameConfig.BASE_CARROTS,
        rowCarrotStep = GameConfig.ROW_CARROT_STEP
    )

    val bankRun = BankRunUseCase(fullBasketBonus = GameConfig.FULL_BASKET_BONUS)

    val claimDailyBonus = ClaimDailyBonusUseCase(
        ladderDays = GameConfig.DAILY_LADDER_DAYS,
        baseCarrots = GameConfig.DAILY_BASE_CARROTS
    )

    val rewardProgress = RewardProgressUseCase(tasks = SampleData.rewardTasks)

    val repository: GardenRepository
        get() = requireNotNull(gardenRepository)

    val viewModelFactory: AppViewModelFactory
        get() = requireNotNull(factory)

    fun init(context: Context) {
        if (gardenRepository != null) return
        val preferences = GardenPreferences(context.applicationContext)
        val created = GardenRepositoryImpl(
            preferences = preferences,
            charmIds = SampleData.charms.map { it.id }
        )
        gardenRepository = created
        factory = AppViewModelFactory(created)
    }
}
