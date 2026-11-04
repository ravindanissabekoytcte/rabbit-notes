package com.Kolqamo5.Vanmow.presentation.collection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Kolqamo5.Vanmow.data.sample.SampleData
import com.Kolqamo5.Vanmow.domain.repository.GardenRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CollectionViewModel(
    private val repository: GardenRepository
) : ViewModel() {

    private val state = MutableStateFlow(CollectionUiState())
    val uiState: StateFlow<CollectionUiState> = state.asStateFlow()

    init {
        viewModelScope.launch {
            repository.profile.collect { profile ->
                val entries = SampleData.charms.map { charm ->
                    CharmEntry(charm = charm, owned = profile.ownedCharms.contains(charm.id))
                }
                state.update { current ->
                    current.copy(
                        entries = entries,
                        ownedCount = entries.count { it.owned },
                        totalCount = entries.size
                    )
                }
            }
        }
    }

    fun select(charmId: Int) {
        val entry = state.value.entries.firstOrNull { it.charm.id == charmId } ?: return
        if (!entry.owned) return
        state.update { it.copy(selected = entry.charm) }
    }

    fun dismiss() {
        state.update { it.copy(selected = null) }
    }
}
