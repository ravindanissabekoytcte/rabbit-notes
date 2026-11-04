package com.Kolqamo5.Vanmow.domain.repository

import com.Kolqamo5.Vanmow.domain.model.PlayerProfile
import com.Kolqamo5.Vanmow.domain.model.RunOutcome
import kotlinx.coroutines.flow.StateFlow

interface GardenRepository {

    val profile: StateFlow<PlayerProfile>

    fun applyRunOutcome(outcome: RunOutcome)

    fun claimDailyBonus(dayIndex: Long, carrots: Int, charms: Int)

    fun claimTask(taskId: Int, payout: Int)

    fun unlockCharm(charmId: Int)

    fun setSoundOn(enabled: Boolean)

    fun setHapticsOn(enabled: Boolean)

    fun setReducedMotion(enabled: Boolean)

    fun resetProgress()
}
