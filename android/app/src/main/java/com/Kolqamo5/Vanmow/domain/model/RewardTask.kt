package com.Kolqamo5.Vanmow.domain.model

enum class RewardMetric {
    ROWS_IN_ONE_RUN,
    TOTAL_CARROTS,
    TOTAL_CHARMS,
    FULL_BASKETS,
    TOTAL_RUNS
}

data class RewardTask(
    val id: Int,
    val titleRes: Int,
    val metric: RewardMetric,
    val target: Int,
    val payout: Int
)

data class RewardTaskProgress(
    val task: RewardTask,
    val current: Int,
    val claimed: Boolean
) {
    val fraction: Float
        get() = if (task.target <= 0) 0f else (current.toFloat() / task.target).coerceIn(0f, 1f)

    val completed: Boolean get() = current >= task.target
}
