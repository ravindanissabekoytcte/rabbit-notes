package com.Kolqamo5.Vanmow.presentation.gameover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Kolqamo5.Vanmow.domain.repository.GardenRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GameOverViewModel(
    private val repository: GardenRepository
) : ViewModel() {

    private val state = MutableStateFlow(GameOverUiState())
    val uiState: StateFlow<GameOverUiState> = state.asStateFlow()

    init {
        viewModelScope.launch {
            repository.profile.collect { profile ->
                state.value = GameOverUiState(
                    bestRow = profile.bestRow,
                    balance = profile.carrots,
                    totalCharms = profile.charms
                )
            }
        }
    }
}
