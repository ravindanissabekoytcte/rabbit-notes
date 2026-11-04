package com.Kolqamo5.Vanmow.presentation.app

import androidx.lifecycle.ViewModel
import com.Kolqamo5.Vanmow.core.navigation.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AppViewModel : ViewModel() {

    private val backStack = mutableListOf<Screen>()

    private val current = MutableStateFlow<Screen>(Screen.Splash)
    val screen: StateFlow<Screen> = current.asStateFlow()

    private val canGoBackState = MutableStateFlow(false)
    val canGoBack: StateFlow<Boolean> = canGoBackState.asStateFlow()

    fun navigate(target: Screen) {
        val from = current.value
        if (from != Screen.Splash) {
            backStack.add(from)
        }
        current.value = target
        syncBackState()
    }

    fun replace(target: Screen) {
        current.value = target
        syncBackState()
    }

    fun resetTo(target: Screen) {
        backStack.clear()
        current.value = target
        syncBackState()
    }

    fun back(): Boolean {
        if (backStack.isEmpty()) return false
        val previous = backStack.removeAt(backStack.lastIndex)
        current.value = previous
        syncBackState()
        return true
    }

    private fun syncBackState() {
        canGoBackState.value = backStack.isNotEmpty()
    }
}
