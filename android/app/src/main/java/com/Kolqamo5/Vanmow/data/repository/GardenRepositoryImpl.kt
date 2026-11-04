package com.Kolqamo5.Vanmow.data.repository

import com.Kolqamo5.Vanmow.core.config.GameConfig
import com.Kolqamo5.Vanmow.data.local.GardenPreferences
import com.Kolqamo5.Vanmow.domain.model.PlayerProfile
import com.Kolqamo5.Vanmow.domain.model.RunOutcome
import com.Kolqamo5.Vanmow.domain.repository.GardenRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class GardenRepositoryImpl(
    private val preferences: GardenPreferences,
    private val charmIds: List<Int>
) : GardenRepository {

    private val state = MutableStateFlow(preferences.load())

    override val profile: StateFlow<PlayerProfile> = state.asStateFlow()

    override fun applyRunOutcome(outcome: RunOutcome) {
        update { current ->
            val owned = unlockNext(current.ownedCharms, outcome.charms)
            current.copy(
                carrots = current.carrots + outcome.carrots,
                totalCarrots = current.totalCarrots + outcome.carrots,
                charms = owned.size,
                runs = current.runs + 1,
                fullBaskets = current.fullBaskets + if (outcome.isFullBasket) 1 else 0,
                bestRow = maxOf(current.bestRow, outcome.clearedRows),
                ownedCharms = owned,
                runHistory = (current.runHistory + outcome.clearedRows)
                    .takeLast(GameConfig.RUN_HISTORY_SIZE)
            )
        }
    }

    override fun claimDailyBonus(dayIndex: Long, carrots: Int, charms: Int) {
        update { current ->
            val continued = current.lastClaimDay == dayIndex - 1
            val owned = unlockNext(current.ownedCharms, charms)
            current.copy(
                carrots = current.carrots + carrots,
                totalCarrots = current.totalCarrots + carrots,
                charms = owned.size,
                ownedCharms = owned,
                lastClaimDay = dayIndex,
                dailyStreak = if (continued) current.dailyStreak + 1 else 1
            )
        }
    }

    override fun claimTask(taskId: Int, payout: Int) {
        update { current ->
            if (current.claimedTasks.contains(taskId)) {
                current
            } else {
                current.copy(
                    carrots = current.carrots + payout,
                    totalCarrots = current.totalCarrots + payout,
                    claimedTasks = current.claimedTasks + taskId
                )
            }
        }
    }

    override fun unlockCharm(charmId: Int) {
        update { current ->
            val owned = current.ownedCharms + charmId
            current.copy(ownedCharms = owned, charms = owned.size)
        }
    }

    override fun setSoundOn(enabled: Boolean) = update { it.copy(soundOn = enabled) }

    override fun setHapticsOn(enabled: Boolean) = update { it.copy(hapticsOn = enabled) }

    override fun setReducedMotion(enabled: Boolean) = update { it.copy(reducedMotion = enabled) }

    override fun resetProgress() {
        preferences.clear()
        val fresh = preferences.load()
        state.value = fresh.copy(
            soundOn = state.value.soundOn,
            hapticsOn = state.value.hapticsOn,
            reducedMotion = state.value.reducedMotion
        )
        preferences.save(state.value)
    }

    private fun unlockNext(owned: Set<Int>, amount: Int): Set<Int> {
        if (amount <= 0) return owned
        val locked = charmIds.filterNot { owned.contains(it) }
        if (locked.isEmpty()) return owned
        return owned + locked.take(amount)
    }

    private fun update(transform: (PlayerProfile) -> PlayerProfile) {
        val next = transform(state.value)
        state.value = next
        preferences.save(next)
    }
}
