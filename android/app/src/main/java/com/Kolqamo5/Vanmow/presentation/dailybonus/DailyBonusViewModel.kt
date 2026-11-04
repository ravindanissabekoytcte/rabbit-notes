package com.Kolqamo5.Vanmow.presentation.dailybonus

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Kolqamo5.Vanmow.core.config.GameConfig
import com.Kolqamo5.Vanmow.domain.repository.GardenRepository
import com.Kolqamo5.Vanmow.domain.usecase.ClaimDailyBonusUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DailyBonusViewModel(
    private val repository: GardenRepository,
    private val claimDailyBonus: ClaimDailyBonusUseCase
) : ViewModel() {

    private val state = MutableStateFlow(DailyBonusUiState())
    val uiState: StateFlow<DailyBonusUiState> = state.asStateFlow()

    init {
        viewModelScope.launch {
            repository.profile.collect { profile ->
                val bonus = claimDailyBonus.stateFor(profile, System.currentTimeMillis())
                state.value = DailyBonusUiState(
                    ladderPosition = bonus.ladderPosition,
                    ladderDays = GameConfig.DAILY_LADDER_DAYS,
                    claimedDays = if (bonus.claimedToday) {
                        bonus.ladderPosition
                    } else {
                        bonus.ladderPosition - 1
                    },
                    carrots = bonus.carrots,
                    charms = bonus.charms,
                    claimedToday = bonus.claimedToday,
                    balance = profile.carrots
                )
            }
        }
    }

    fun claim() {
        val current = state.value
        if (current.claimedToday) return
        viewModelScope.launch {
            val profile = repository.profile.value
            val bonus = claimDailyBonus.stateFor(profile, System.currentTimeMillis())
            if (bonus.claimedToday) return@launch
            repository.claimDailyBonus(
                dayIndex = bonus.dayIndex,
                carrots = bonus.carrots,
                charms = bonus.charms
            )
        }
    }
}
