package com.Kolqamo5.Vanmow.domain.model

data class RunOutcome(
    val carrots: Int,
    val charms: Int,
    val clearedRows: Int,
    val isFullBasket: Boolean,
    val isNewBest: Boolean
)
