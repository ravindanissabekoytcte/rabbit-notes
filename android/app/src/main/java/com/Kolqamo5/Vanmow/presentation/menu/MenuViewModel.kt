package com.Kolqamo5.Vanmow.presentation.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Kolqamo5.Vanmow.domain.repository.GardenRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MenuViewModel(
    private val repository: GardenRepository
) : ViewModel() {

    private val state = MutableStateFlow(MenuUiState())
    val uiState: StateFlow<MenuUiState> = state.asStateFlow()

    init {
        viewModelScope.launch {
            repository.profile.collect { profile ->
                state.value = MenuUiState(
                    carrots = profile.carrots,
                    bestRun = profile.bestRow,
                    charms = profile.charms
                )
            }
        }
    }
}
