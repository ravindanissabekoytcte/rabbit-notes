package com.Kolqamo5.Vanmow.data.local

import android.content.Context
import android.content.SharedPreferences
import com.Kolqamo5.Vanmow.domain.model.PlayerProfile

class GardenPreferences(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("fortune_rabbit_garden", Context.MODE_PRIVATE)

    fun load(): PlayerProfile = PlayerProfile(
        carrots = prefs.getInt(KEY_CARROTS, 0),
        totalCarrots = prefs.getInt(KEY_TOTAL_CARROTS, 0),
        charms = prefs.getInt(KEY_CHARMS, 0),
        runs = prefs.getInt(KEY_RUNS, 0),
        fullBaskets = prefs.getInt(KEY_FULL_BASKETS, 0),
        bestRow = prefs.getInt(KEY_BEST_ROW, 0),
        ownedCharms = readIntSet(KEY_OWNED_CHARMS),
        claimedTasks = readIntSet(KEY_CLAIMED_TASKS),
        runHistory = readIntList(KEY_RUN_HISTORY),
        lastClaimDay = prefs.getLong(KEY_LAST_CLAIM_DAY, 0L),
        dailyStreak = prefs.getInt(KEY_DAILY_STREAK, 0),
        soundOn = prefs.getBoolean(KEY_SOUND, true),
        hapticsOn = prefs.getBoolean(KEY_HAPTICS, true),
        reducedMotion = prefs.getBoolean(KEY_REDUCED_MOTION, false)
    )

    fun save(profile: PlayerProfile) {
        prefs.edit()
            .putInt(KEY_CARROTS, profile.carrots)
            .putInt(KEY_TOTAL_CARROTS, profile.totalCarrots)
            .putInt(KEY_CHARMS, profile.charms)
            .putInt(KEY_RUNS, profile.runs)
            .putInt(KEY_FULL_BASKETS, profile.fullBaskets)
            .putInt(KEY_BEST_ROW, profile.bestRow)
            .putString(KEY_OWNED_CHARMS, profile.ownedCharms.joinToString(SEPARATOR))
            .putString(KEY_CLAIMED_TASKS, profile.claimedTasks.joinToString(SEPARATOR))
            .putString(KEY_RUN_HISTORY, profile.runHistory.joinToString(SEPARATOR))
            .putLong(KEY_LAST_CLAIM_DAY, profile.lastClaimDay)
            .putInt(KEY_DAILY_STREAK, profile.dailyStreak)
            .putBoolean(KEY_SOUND, profile.soundOn)
            .putBoolean(KEY_HAPTICS, profile.hapticsOn)
            .putBoolean(KEY_REDUCED_MOTION, profile.reducedMotion)
            .apply()
    }

    fun clear() {
        prefs.edit().clear().apply()
    }

    private fun readIntSet(key: String): Set<Int> = readIntList(key).toSet()

    private fun readIntList(key: String): List<Int> {
        val raw = prefs.getString(key, "").orEmpty()
        if (raw.isBlank()) return emptyList()
        return raw.split(SEPARATOR).mapNotNull { it.trim().toIntOrNull() }
    }

    private companion object {
        const val SEPARATOR = ","
        const val KEY_CARROTS = "carrots"
        const val KEY_TOTAL_CARROTS = "total_carrots"
        const val KEY_CHARMS = "charms"
        const val KEY_RUNS = "runs"
        const val KEY_FULL_BASKETS = "full_baskets"
        const val KEY_BEST_ROW = "best_row"
        const val KEY_OWNED_CHARMS = "owned_charms"
        const val KEY_CLAIMED_TASKS = "claimed_tasks"
        const val KEY_RUN_HISTORY = "run_history"
        const val KEY_LAST_CLAIM_DAY = "last_claim_day"
        const val KEY_DAILY_STREAK = "daily_streak"
        const val KEY_SOUND = "sound_on"
        const val KEY_HAPTICS = "haptics_on"
        const val KEY_REDUCED_MOTION = "reduced_motion"
    }
}
