package com.Kolqamo5.Vanmow.domain.usecase

import com.Kolqamo5.Vanmow.domain.model.PlayerProfile
import com.Kolqamo5.Vanmow.domain.model.RewardMetric
import com.Kolqamo5.Vanmow.domain.model.RewardTask
import com.Kolqamo5.Vanmow.domain.model.RewardTaskProgress

class RewardProgressUseCase(
    private val tasks: List<RewardTask>
) {

    operator fun invoke(profile: PlayerProfile): List<RewardTaskProgress> = tasks.map { task ->
        RewardTaskProgress(
            task = task,
            current = currentFor(task.metric, profile).coerceAtMost(task.target),
            claimed = profile.claimedTasks.contains(task.id)
        )
    }

    private fun currentFor(metric: RewardMetric, profile: PlayerProfile): Int = when (metric) {
        RewardMetric.ROWS_IN_ONE_RUN -> profile.bestRow
        RewardMetric.TOTAL_CARROTS -> profile.totalCarrots
        RewardMetric.TOTAL_CHARMS -> profile.charms
        RewardMetric.FULL_BASKETS -> profile.fullBaskets
        RewardMetric.TOTAL_RUNS -> profile.runs
    }
}
