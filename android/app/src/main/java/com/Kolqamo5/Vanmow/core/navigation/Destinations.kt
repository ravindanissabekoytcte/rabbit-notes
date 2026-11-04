package com.Kolqamo5.Vanmow.core.navigation

sealed interface Screen {

    data object Splash : Screen

    data object Menu : Screen

    data object Game : Screen

    data class GameOver(
        val carrots: Int,
        val charms: Int,
        val patches: Int,
        val isFullBasket: Boolean,
        val isNewBest: Boolean
    ) : Screen

    data object DailyBonus : Screen

    data object Rewards : Screen

    data object Progress : Screen

    data object Collection : Screen

    data object Settings : Screen
}
