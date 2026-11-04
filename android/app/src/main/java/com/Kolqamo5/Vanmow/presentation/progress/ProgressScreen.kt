package com.Kolqamo5.Vanmow.presentation.progress

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Kolqamo5.Vanmow.R
import com.Kolqamo5.Vanmow.core.di.ServiceLocator
import com.Kolqamo5.Vanmow.core.theme.AccentGold
import com.Kolqamo5.Vanmow.core.theme.BackgroundBase
import com.Kolqamo5.Vanmow.core.theme.BrandPrimary
import com.Kolqamo5.Vanmow.core.theme.SecondaryMint
import com.Kolqamo5.Vanmow.core.theme.SurfaceAlt
import com.Kolqamo5.Vanmow.core.theme.TextPrimary
import com.Kolqamo5.Vanmow.core.theme.TextSecondary
import com.Kolqamo5.Vanmow.core.ui.ClayCard
import com.Kolqamo5.Vanmow.core.ui.ProgressBarClay
import com.Kolqamo5.Vanmow.core.ui.ScreenTopBar
import com.Kolqamo5.Vanmow.core.ui.StatCard

@Composable
fun ProgressRoute(onBack: () -> Unit) {
    val progressViewModel: ProgressViewModel = viewModel(factory = ServiceLocator.viewModelFactory)
    val state by progressViewModel.uiState.collectAsStateWithLifecycle()

    ProgressScreen(state = state, onBack = onBack)
}

@Composable
fun ProgressScreen(
    state: ProgressUiState,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(BackgroundBase, SurfaceAlt)))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
        ) {
            ScreenTopBar(
                title = stringResource(R.string.progress),
                onBack = onBack
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                ClayCard(modifier = Modifier.fillMaxWidth(), radius = 28.dp) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.garden_level, state.level),
                            style = MaterialTheme.typography.titleMedium,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        ProgressBarClay(
                            fraction = state.levelFraction,
                            accent = BrandPrimary,
                            accentEnd = AccentGold,
                            barHeight = 12.dp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = stringResource(
                                R.string.level_progress,
                                state.carrotsIntoLevel,
                                state.carrotsPerLevel
                            ),
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard(
                        label = stringResource(R.string.stat_runs),
                        value = state.runs.toString(),
                        accent = BrandPrimary,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        label = stringResource(R.string.stat_best_run),
                        value = state.bestRow.toString(),
                        accent = AccentGold,
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard(
                        label = stringResource(R.string.stat_carrots),
                        value = state.carrots.toString(),
                        accent = AccentGold,
                        modifier = Modifier.weight(1f)
                    )
                    StatCard(
                        label = stringResource(R.string.stat_charms),
                        value = state.charms.toString(),
                        accent = SecondaryMint,
                        modifier = Modifier.weight(1f)
                    )
                }

                if (state.runHistory.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(22.dp))
                    Text(
                        text = stringResource(R.string.last_runs),
                        style = MaterialTheme.typography.labelSmall,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    RunHistoryChart(
                        history = state.runHistory,
                        rowsPerRun = state.rowsPerRun
                    )
                }
            }
        }
    }
}

@Composable
private fun RunHistoryChart(
    history: List<Int>,
    rowsPerRun: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(130.dp),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        history.forEach { cleared ->
            val fraction = (cleared.toFloat() / rowsPerRun).coerceIn(0.08f, 1f)
            val full = cleared >= rowsPerRun
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(fraction)
                            .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                            .background(if (full) SecondaryMint else BrandPrimary)
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = cleared.toString(),
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
