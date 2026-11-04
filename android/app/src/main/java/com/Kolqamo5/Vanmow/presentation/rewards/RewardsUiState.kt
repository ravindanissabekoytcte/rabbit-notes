package com.Kolqamo5.Vanmow.presentation.rewards

import com.Kolqamo5.Vanmow.domain.model.RewardTaskProgress

data class RewardsUiState(
    val tasks: List<RewardTaskProgress> = emptyList(),
    val balance: Int = 0
) {
    val allClaimed: Boolean get() = tasks.isNotEmpty() && tasks.all { it.claimed }
}
