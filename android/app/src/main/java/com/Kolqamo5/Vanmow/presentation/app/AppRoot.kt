package com.Kolqamo5.Vanmow.presentation.app

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Kolqamo5.Vanmow.core.di.ServiceLocator
import com.Kolqamo5.Vanmow.core.navigation.Screen
import com.Kolqamo5.Vanmow.presentation.collection.CollectionRoute
import com.Kolqamo5.Vanmow.presentation.dailybonus.DailyBonusRoute
import com.Kolqamo5.Vanmow.presentation.game.GameRoute
import com.Kolqamo5.Vanmow.presentation.gameover.GameOverRoute
import com.Kolqamo5.Vanmow.presentation.menu.MenuRoute
import com.Kolqamo5.Vanmow.presentation.progress.ProgressRoute
import com.Kolqamo5.Vanmow.presentation.rewards.RewardsRoute
import com.Kolqamo5.Vanmow.presentation.settings.SettingsRoute
import com.Kolqamo5.Vanmow.presentation.splash.SplashRoute

@Composable
fun AppRoot() {
    val appViewModel: AppViewModel = viewModel(factory = ServiceLocator.viewModelFactory)
    val screen by appViewModel.screen.collectAsStateWithLifecycle()
    val canGoBack by appViewModel.canGoBack.collectAsStateWithLifecycle()

    BackHandler(enabled = canGoBack) {
        appViewModel.back()
    }

    AnimatedContent(
        targetState = screen,
        transitionSpec = {
            if (initialState == Screen.Menu && targetState == Screen.Game) {
                (slideInVertically { full -> full / 6 } + fadeIn(tween(360)))
                    .togetherWith(fadeOut(tween(260)))
            } else {
                fadeIn(tween(420)) togetherWith fadeOut(tween(320))
            }
        },
        label = "screenTransition"
    ) { target ->
        Box(modifier = Modifier.fillMaxSize()) {
            when (target) {
                Screen.Splash -> SplashRoute(
                    onFinished = { appViewModel.resetTo(Screen.Menu) }
                )

                Screen.Menu -> MenuRoute(
                    onPlay = { appViewModel.navigate(Screen.Game) },
                    onDailyBonus = { appViewModel.navigate(Screen.DailyBonus) },
                    onRewards = { appViewModel.navigate(Screen.Rewards) },
                    onCollection = { appViewModel.navigate(Screen.Collection) },
                    onProgress = { appViewModel.navigate(Screen.Progress) },
                    onSettings = { appViewModel.navigate(Screen.Settings) }
                )

                Screen.Game -> GameRoute(
                    onGameOver = { result ->
                        appViewModel.resetTo(Screen.Menu)
                        appViewModel.navigate(result)
                    },
                    onMenu = { appViewModel.resetTo(Screen.Menu) }
                )

                is Screen.GameOver -> GameOverRoute(
                    result = target,
                    onPlayAgain = { appViewModel.replace(Screen.Game) },
                    onMenu = { appViewModel.resetTo(Screen.Menu) }
                )

                Screen.DailyBonus -> DailyBonusRoute(onBack = { appViewModel.back() })

                Screen.Rewards -> RewardsRoute(onBack = { appViewModel.back() })

                Screen.Progress -> ProgressRoute(onBack = { appViewModel.back() })

                Screen.Collection -> CollectionRoute(onBack = { appViewModel.back() })

                Screen.Settings -> SettingsRoute(onBack = { appViewModel.back() })
            }
        }
    }
}
