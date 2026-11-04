package com.Kolqamo5.Vanmow.domain.usecase

import com.Kolqamo5.Vanmow.domain.model.PlayerProfile

data class DailyBonusState(
    val dayIndex: Long,
    val ladderPosition: Int,
    val carrots: Int,
    val charms: Int,
    val claimedToday: Boolean
)

class ClaimDailyBonusUseCase(
    private val ladderDays: Int,
    private val baseCarrots: Int
) {

    private val dayLengthMillis = 86_400_000L

    fun stateFor(profile: PlayerProfile, nowMillis: Long): DailyBonusState {
        val today = nowMillis / dayLengthMillis
        val claimedToday = profile.lastClaimDay == today
        val streak = if (claimedToday) {
            profile.dailyStreak
        } else {
            nextStreak(profile, today)
        }
        val position = ((streak - 1).coerceAtLeast(0) % ladderDays) + 1
        return DailyBonusState(
            dayIndex = today,
            ladderPosition = position,
            carrots = baseCarrots * position,
            charms = if (position % 3 == 0) 1 else 0,
            claimedToday = claimedToday
        )
    }

    private fun nextStreak(profile: PlayerProfile, today: Long): Int {
        val continued = profile.lastClaimDay == today - 1
        return if (continued) profile.dailyStreak + 1 else 1
    }
}
