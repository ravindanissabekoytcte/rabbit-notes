package com.Kolqamo5.Vanmow.presentation.collection

import com.Kolqamo5.Vanmow.domain.model.Charm

data class CharmEntry(
    val charm: Charm,
    val owned: Boolean
)

data class CollectionUiState(
    val entries: List<CharmEntry> = emptyList(),
    val ownedCount: Int = 0,
    val totalCount: Int = 0,
    val selected: Charm? = null
) {
    val fraction: Float
        get() = if (totalCount <= 0) 0f else ownedCount.toFloat() / totalCount
}
