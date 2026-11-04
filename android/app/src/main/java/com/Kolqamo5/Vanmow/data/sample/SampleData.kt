package com.Kolqamo5.Vanmow.data.sample

import com.Kolqamo5.Vanmow.R
import com.Kolqamo5.Vanmow.domain.model.Charm
import com.Kolqamo5.Vanmow.domain.model.RewardMetric
import com.Kolqamo5.Vanmow.domain.model.RewardTask

object SampleData {

    val charms: List<Charm> = listOf(
        Charm(1, R.string.charm_clover_name, R.string.charm_clover_lore),
        Charm(2, R.string.charm_bell_name, R.string.charm_bell_lore),
        Charm(3, R.string.charm_acorn_name, R.string.charm_acorn_lore),
        Charm(4, R.string.charm_dewdrop_name, R.string.charm_dewdrop_lore),
        Charm(5, R.string.charm_ribbon_name, R.string.charm_ribbon_lore),
        Charm(6, R.string.charm_lantern_name, R.string.charm_lantern_lore),
        Charm(7, R.string.charm_feather_name, R.string.charm_feather_lore),
        Charm(8, R.string.charm_pebble_name, R.string.charm_pebble_lore),
        Charm(9, R.string.charm_thimble_name, R.string.charm_thimble_lore),
        Charm(10, R.string.charm_marigold_name, R.string.charm_marigold_lore),
        Charm(11, R.string.charm_keyring_name, R.string.charm_keyring_lore),
        Charm(12, R.string.charm_moonseed_name, R.string.charm_moonseed_lore)
    )

    val rewardTasks: List<RewardTask> = listOf(
        RewardTask(1, R.string.task_clear_rows, RewardMetric.ROWS_IN_ONE_RUN, 3, 40),
        RewardTask(2, R.string.task_collect_carrots, RewardMetric.TOTAL_CARROTS, 50, 60),
        RewardTask(3, R.string.task_find_charms, RewardMetric.TOTAL_CHARMS, 2, 55),
        RewardTask(4, R.string.task_full_basket, RewardMetric.FULL_BASKETS, 1, 120),
        RewardTask(5, R.string.task_play_runs, RewardMetric.TOTAL_RUNS, 5, 75)
    )
}
