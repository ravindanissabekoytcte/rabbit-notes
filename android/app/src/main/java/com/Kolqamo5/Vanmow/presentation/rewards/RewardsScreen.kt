package com.Kolqamo5.Vanmow.presentation.rewards

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Kolqamo5.Vanmow.R
import com.Kolqamo5.Vanmow.core.di.ServiceLocator
import com.Kolqamo5.Vanmow.core.theme.AccentGold
import com.Kolqamo5.Vanmow.core.theme.AccentGoldDeep
import com.Kolqamo5.Vanmow.core.theme.BackgroundBase
import com.Kolqamo5.Vanmow.core.theme.SecondaryMintDeep
import com.Kolqamo5.Vanmow.core.theme.SurfaceAlt
import com.Kolqamo5.Vanmow.core.theme.TextPrimary
import com.Kolqamo5.Vanmow.core.theme.TextSecondary
import com.Kolqamo5.Vanmow.core.ui.AppIcons
import com.Kolqamo5.Vanmow.core.ui.CarrotPill
import com.Kolqamo5.Vanmow.core.ui.ClayCard
import com.Kolqamo5.Vanmow.core.ui.ProgressBarClay
import com.Kolqamo5.Vanmow.core.ui.ScreenTopBar
import com.Kolqamo5.Vanmow.core.ui.SecondaryButton
import com.Kolqamo5.Vanmow.domain.model.RewardMetric
import com.Kolqamo5.Vanmow.domain.model.RewardTaskProgress

@Composable
fun RewardsRoute(onBack: () -> Unit) {
    val rewardsViewModel: RewardsViewModel = viewModel(factory = ServiceLocator.viewModelFactory)
    val state by rewardsViewModel.uiState.collectAsStateWithLifecycle()

    RewardsScreen(
        state = state,
        onClaim = rewardsViewModel::claim,
        onBack = onBack
    )
}

@Composable
fun RewardsScreen(
    state: RewardsUiState,
    onClaim: (Int) -> Unit,
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
                title = stringResource(R.string.rewards),
                onBack = onBack
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.End
            ) {
                CarrotPill(count = state.balance, height = 40.dp, spriteSize = 20.dp)
            }
            Spacer(modifier = Modifier.height(12.dp))

            if (state.allClaimed) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.sprite_1),
                        contentDescription = stringResource(R.string.cd_rabbit),
                        modifier = Modifier
                            .size(160.dp)
                            .alpha(0.6f),
                        contentScale = ContentScale.Fit
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = stringResource(R.string.rewards_empty),
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextSecondary,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 36.dp)
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(state.tasks, key = { it.task.id }) { entry ->
                        RewardRow(entry = entry, onClaim = onClaim)
                    }
                }
            }
        }
    }
}

@Composable
private fun RewardRow(
    entry: RewardTaskProgress,
    onClaim: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val sprite = when (entry.task.metric) {
        RewardMetric.TOTAL_CARROTS -> R.drawable.sprite_2
        RewardMetric.TOTAL_CHARMS -> R.drawable.sprite_3
        else -> R.drawable.sprite_4
    }

    ClayCard(
        modifier = modifier.fillMaxWidth(),
        radius = 24.dp,
        elevation = 10.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(92.dp)
                .padding(horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Image(
                painter = painterResource(sprite),
                contentDescription = null,
                modifier = Modifier.size(56.dp),
                contentScale = ContentScale.Fit
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(entry.task.titleRes),
                    style = MaterialTheme.typography.labelLarge,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.height(6.dp))
                ProgressBarClay(
                    fraction = entry.fraction,
                    accent = AccentGold,
                    accentEnd = AccentGoldDeep
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(
                        R.string.reward_progress,
                        entry.current,
                        entry.task.target
                    ),
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary
                )
            }
            if (entry.claimed) {
                Icon(
                    painter = painterResource(AppIcons.Check),
                    contentDescription = stringResource(R.string.state_claimed),
                    modifier = Modifier.size(28.dp),
                    tint = SecondaryMintDeep
                )
            } else {
                SecondaryButton(
                    text = stringResource(R.string.claim),
                    onClick = { onClaim(entry.task.id) },
                    enabled = entry.completed,
                    height = 48.dp
                )
            }
        }
    }
}
