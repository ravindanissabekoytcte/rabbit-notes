package com.Kolqamo5.Vanmow.presentation.settings

data class SettingsUiState(
    val soundOn: Boolean = true,
    val hapticsOn: Boolean = true,
    val reducedMotion: Boolean = false,
    val confirmResetVisible: Boolean = false
)
