package com.Kolqamo5.Vanmow.presentation.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Kolqamo5.Vanmow.core.config.GameConfig
import com.Kolqamo5.Vanmow.domain.model.GardenRun
import com.Kolqamo5.Vanmow.domain.model.RunOutcome
import com.Kolqamo5.Vanmow.domain.repository.GardenRepository
import com.Kolqamo5.Vanmow.domain.usecase.BankRunUseCase
import com.Kolqamo5.Vanmow.domain.usecase.RevealPatchUseCase
import com.Kolqamo5.Vanmow.domain.usecase.StartRunUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

data class GameFinished(
    val carrots: Int,
    val charms: Int,
    val patches: Int,
    val isFullBasket: Boolean,
    val isNewBest: Boolean
)

class GameViewModel(
    private val repository: GardenRepository,
    private val startRun: StartRunUseCase,
    private val revealPatch: RevealPatchUseCase,
    private val bankRun: BankRunUseCase
) : ViewModel() {

    private val state = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = state.asStateFlow()

    private val finishedChannel = Channel<GameFinished>(Channel.BUFFERED)
    val finishedEvents: Flow<GameFinished> = finishedChannel.receiveAsFlow()

    private var currentRun: GardenRun = startRun()
    private var timerJob: Job? = null
    private var settled = false

    init {
        publish(currentRun, GamePhase.READY)
    }

    fun attach() {
        if (settled) {
            beginRun()
        } else {
            resume()
        }
    }

    fun pause() {
        timerJob?.cancel()
        timerJob = null
        state.value = state.value.copy(paused = true)
    }

    fun resume() {
        val current = state.value
        val phase = if (current.phase == GamePhase.HOPPING) {
            if (current.clearedRows > 0) GamePhase.RUNNING else GamePhase.READY
        } else {
            current.phase
        }
        state.value = current.copy(paused = false, phase = phase, shaking = false)
    }

    fun onPatchTap(column: Int) {
        val current = state.value
        if (!current.canTap || settled) return
        if (column !in 0 until currentRun.board[currentRun.activeRow - 1].size) return

        val result = revealPatch(currentRun, column)
        currentRun = result.run

        when {
            result.caught -> {
                settled = true
                publish(currentRun, GamePhase.CAUGHT, shaking = true)
                scheduleFinish(GameConfig.CAUGHT_DELAY_MS) {
                    bankRun.caught(currentRun, repository.profile.value.bestRow)
                }
            }

            result.fullBasket -> {
                settled = true
                publish(currentRun, GamePhase.FULL_BASKET)
                scheduleFinish(GameConfig.BANK_DELAY_MS) {
                    bankRun(currentRun, repository.profile.value.bestRow)
                }
            }

            else -> {
                publish(currentRun, GamePhase.HOPPING)
                timerJob?.cancel()
                timerJob = viewModelScope.launch {
                    delay(GameConfig.HOP_LOCK_MS)
                    publish(currentRun, GamePhase.RUNNING)
                }
            }
        }
    }

    fun onCollect() {
        val current = state.value
        if (!current.canCollect || settled) return
        settled = true
        publish(currentRun, GamePhase.BANKED)
        scheduleFinish(GameConfig.BANK_DELAY_MS) {
            bankRun(currentRun, repository.profile.value.bestRow)
        }
    }

    private fun beginRun() {
        timerJob?.cancel()
        timerJob = null
        settled = false
        currentRun = startRun()
        publish(currentRun, GamePhase.READY)
    }

    private fun scheduleFinish(delayMillis: Long, outcome: () -> RunOutcome) {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            delay(delayMillis)
            val result = outcome()
            repository.applyRunOutcome(result)
            finishedChannel.send(
                GameFinished(
                    carrots = result.carrots,
                    charms = result.charms,
                    patches = result.clearedRows,
                    isFullBasket = result.isFullBasket,
                    isNewBest = result.isNewBest
                )
            )
        }
    }

    private fun publish(source: GardenRun, phase: GamePhase, shaking: Boolean = false) {
        state.value = state.value.copy(
            board = source.board,
            rows = source.rows,
            columns = source.board.firstOrNull()?.size ?: 0,
            activeRow = source.activeRow,
            clearedRows = source.clearedRows,
            runCarrots = source.carrots,
            runCharms = source.charms,
            balance = repository.profile.value.carrots,
            multiplier = source.multiplier,
            phase = phase,
            paused = false,
            shaking = shaking
        )
    }

    override fun onCleared() {
        timerJob?.cancel()
        finishedChannel.close()
        super.onCleared()
    }
}
