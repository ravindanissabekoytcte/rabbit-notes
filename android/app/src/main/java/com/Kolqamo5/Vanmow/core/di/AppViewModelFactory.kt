package com.Kolqamo5.Vanmow.core.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.Kolqamo5.Vanmow.domain.repository.GardenRepository
import com.Kolqamo5.Vanmow.presentation.app.AppViewModel
import com.Kolqamo5.Vanmow.presentation.collection.CollectionViewModel
import com.Kolqamo5.Vanmow.presentation.dailybonus.DailyBonusViewModel
import com.Kolqamo5.Vanmow.presentation.game.GameViewModel
import com.Kolqamo5.Vanmow.presentation.gameover.GameOverViewModel
import com.Kolqamo5.Vanmow.presentation.menu.MenuViewModel
import com.Kolqamo5.Vanmow.presentation.progress.ProgressViewModel
import com.Kolqamo5.Vanmow.presentation.rewards.RewardsViewModel
import com.Kolqamo5.Vanmow.presentation.settings.SettingsViewModel
import com.Kolqamo5.Vanmow.presentation.splash.SplashViewModel

class AppViewModelFactory(
    private val repository: GardenRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = when {
        modelClass.isAssignableFrom(AppViewModel::class.java) ->
            AppViewModel() as T

        modelClass.isAssignableFrom(SplashViewModel::class.java) ->
            SplashViewModel() as T

        modelClass.isAssignableFrom(MenuViewModel::class.java) ->
            MenuViewModel(repository) as T

        modelClass.isAssignableFrom(GameViewModel::class.java) ->
            GameViewModel(
                repository = repository,
                startRun = ServiceLocator.startRun,
                revealPatch = ServiceLocator.revealPatch,
                bankRun = ServiceLocator.bankRun
            ) as T

        modelClass.isAssignableFrom(GameOverViewModel::class.java) ->
            GameOverViewModel(repository) as T

        modelClass.isAssignableFrom(DailyBonusViewModel::class.java) ->
            DailyBonusViewModel(repository, ServiceLocator.claimDailyBonus) as T

        modelClass.isAssignableFrom(RewardsViewModel::class.java) ->
            RewardsViewModel(repository, ServiceLocator.rewardProgress) as T

        modelClass.isAssignableFrom(ProgressViewModel::class.java) ->
            ProgressViewModel(repository) as T

        modelClass.isAssignableFrom(CollectionViewModel::class.java) ->
            CollectionViewModel(repository) as T

        modelClass.isAssignableFrom(SettingsViewModel::class.java) ->
            SettingsViewModel(repository) as T

        else -> throw IllegalArgumentException(modelClass.name)
    }
}
