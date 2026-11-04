package com.Kolqamo5.Vanmow.presentation.dailybonus

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Kolqamo5.Vanmow.R
import com.Kolqamo5.Vanmow.core.di.ServiceLocator
import com.Kolqamo5.Vanmow.core.theme.AccentGold
import com.Kolqamo5.Vanmow.core.theme.AccentGoldDeep
import com.Kolqamo5.Vanmow.core.theme.BackgroundBase
import com.Kolqamo5.Vanmow.core.theme.SecondaryMint
import com.Kolqamo5.Vanmow.core.theme.SurfaceAlt
import com.Kolqamo5.Vanmow.core.theme.SurfaceCard
import com.Kolqamo5.Vanmow.core.theme.TextPrimary
import com.Kolqamo5.Vanmow.core.theme.TextSecondary
import com.Kolqamo5.Vanmow.core.ui.AppIcons
import com.Kolqamo5.Vanmow.core.ui.CarrotPill
import com.Kolqamo5.Vanmow.core.ui.PrimaryButton
import com.Kolqamo5.Vanmow.core.ui.ScreenTopBar

@Composable
fun DailyBonusRoute(onBack: () -> Unit) {
    val dailyBonusViewModel: DailyBonusViewModel =
        viewModel(factory = ServiceLocator.viewModelFactory)
    val state by dailyBonusViewModel.uiState.collectAsStateWithLifecycle()

    DailyBonusScreen(
        state = state,
        onClaim = dailyBonusViewModel::claim,
        onBack = onBack
    )
}

@Composable
fun DailyBonusScreen(
    state: DailyBonusUiState,
    onClaim: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val transition = rememberInfiniteTransition(label = "bonusLoops")
    val shine by transition.animateFloat(
        initialValue = 0.9f,
        targetValue = 1.06f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "basketShine"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(listOf(BackgroundBase, SurfaceAlt))
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ScreenTopBar(
                title = stringResource(R.string.daily_bonus),
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

            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                for (day in 1..state.ladderDays) {
                    DayTile(
                        day = day,
                        claimed = day <= state.claimedDays,
                        isToday = day == state.ladderPosition,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Image(
                painter = painterResource(R.drawable.sprite_4),
                contentDescription = stringResource(R.string.cd_basket),
                modifier = Modifier
                    .size(140.dp)
                    .graphicsLayer {
                        scaleX = shine
                        scaleY = shine
                    },
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.bonus_today, state.carrots, state.charms),
                style = MaterialTheme.typography.bodyLarge,
                color = TextPrimary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 28.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = stringResource(R.string.bonus_hint),
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 28.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))
            PrimaryButton(
                text = stringResource(
                    if (state.claimedToday) R.string.come_back_tomorrow else R.string.open_basket
                ),
                onClick = onClaim,
                iconRes = if (state.claimedToday) AppIcons.Check else AppIcons.Redeem,
                enabled = !state.claimedToday,
                height = 60.dp,
                gradient = listOf(AccentGold, AccentGoldDeep),
                labelColor = TextPrimary,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 28.dp)
            )
        }
    }
}

@Composable
private fun DayTile(
    day: Int,
    claimed: Boolean,
    isToday: Boolean,
    modifier: Modifier = Modifier
) {
    val fill = when {
        claimed -> SecondaryMint
        isToday -> SurfaceCard
        else -> SurfaceAlt.copy(alpha = 0.6f)
    }
    val border = if (isToday && !claimed) {
        BorderStroke(3.dp, AccentGold)
    } else {
        BorderStroke(2.dp, Color.White.copy(alpha = 0.7f))
    }
    val stateText = stringResource(
        when {
            claimed -> R.string.state_claimed
            isToday -> R.string.state_today
            else -> R.string.state_locked
        }
    )

    Surface(
        modifier = modifier
            .height(64.dp)
            .semantics { stateDescription = stateText },
        shape = RoundedCornerShape(18.dp),
        color = fill,
        border = border
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (claimed) {
                Icon(
                    painter = painterResource(AppIcons.Check),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = TextPrimary
                )
                Spacer(modifier = Modifier.height(2.dp))
            }
            Text(
                text = stringResource(R.string.day_label, day),
                style = MaterialTheme.typography.labelSmall,
                color = if (claimed || isToday) TextPrimary else TextSecondary,
                textAlign = TextAlign.Center
            )
        }
    }
}
