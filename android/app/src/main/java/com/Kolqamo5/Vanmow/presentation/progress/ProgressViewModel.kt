package com.Kolqamo5.Vanmow.presentation.progress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Kolqamo5.Vanmow.core.config.GameConfig
import com.Kolqamo5.Vanmow.domain.repository.GardenRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProgressViewModel(
    private val repository: GardenRepository
) : ViewModel() {

    private val state = MutableStateFlow(ProgressUiState())
    val uiState: StateFlow<ProgressUiState> = state.asStateFlow()

    init {
        viewModelScope.launch {
            repository.profile.collect { profile ->
                state.value = ProgressUiState(
                    level = profile.level,
                    carrotsIntoLevel = profile.carrotsIntoLevel,
                    carrotsPerLevel = profile.carrotsPerLevel,
                    runs = profile.runs,
                    bestRow = profile.bestRow,
                    carrots = profile.carrots,
                    charms = profile.charms,
                    runHistory = profile.runHistory,
                    rowsPerRun = GameConfig.ROWS
                )
            }
        }
    }
}
