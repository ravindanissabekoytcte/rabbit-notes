package com.Kolqamo5.Vanmow.presentation.rewards

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Kolqamo5.Vanmow.domain.repository.GardenRepository
import com.Kolqamo5.Vanmow.domain.usecase.RewardProgressUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RewardsViewModel(
    private val repository: GardenRepository,
    private val rewardProgress: RewardProgressUseCase
) : ViewModel() {

    private val state = MutableStateFlow(RewardsUiState())
    val uiState: StateFlow<RewardsUiState> = state.asStateFlow()

    init {
        viewModelScope.launch {
            repository.profile.collect { profile ->
                state.value = RewardsUiState(
                    tasks = rewardProgress(profile),
                    balance = profile.carrots
                )
            }
        }
    }

    fun claim(taskId: Int) {
        val entry = state.value.tasks.firstOrNull { it.task.id == taskId } ?: return
        if (entry.claimed || !entry.completed) return
        repository.claimTask(taskId, entry.task.payout)
    }
}
