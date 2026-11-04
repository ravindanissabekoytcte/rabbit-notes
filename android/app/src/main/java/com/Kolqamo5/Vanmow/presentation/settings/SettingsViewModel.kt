package com.Kolqamo5.Vanmow.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Kolqamo5.Vanmow.domain.repository.GardenRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val repository: GardenRepository
) : ViewModel() {

    private val state = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = state.asStateFlow()

    init {
        viewModelScope.launch {
            repository.profile.collect { profile ->
                state.update { current ->
                    current.copy(
                        soundOn = profile.soundOn,
                        hapticsOn = profile.hapticsOn,
                        reducedMotion = profile.reducedMotion
                    )
                }
            }
        }
    }

    fun setSound(enabled: Boolean) {
        repository.setSoundOn(enabled)
    }

    fun setHaptics(enabled: Boolean) {
        repository.setHapticsOn(enabled)
    }

    fun setReducedMotion(enabled: Boolean) {
        repository.setReducedMotion(enabled)
    }

    fun askReset() {
        state.update { it.copy(confirmResetVisible = true) }
    }

    fun cancelReset() {
        state.update { it.copy(confirmResetVisible = false) }
    }

    fun confirmReset() {
        repository.resetProgress()
        state.update { it.copy(confirmResetVisible = false) }
    }
}
