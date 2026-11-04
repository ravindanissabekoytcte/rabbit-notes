package com.Kolqamo5.Vanmow.presentation.menu

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.graphicsLayer
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
import com.Kolqamo5.Vanmow.core.theme.BackgroundBase
import com.Kolqamo5.Vanmow.core.theme.BrandPrimary
import com.Kolqamo5.Vanmow.core.theme.SecondaryMint
import com.Kolqamo5.Vanmow.core.theme.SurfaceCard
import com.Kolqamo5.Vanmow.core.theme.SurfaceTint
import com.Kolqamo5.Vanmow.core.theme.TextPrimary
import com.Kolqamo5.Vanmow.core.theme.TextSecondary
import com.Kolqamo5.Vanmow.core.ui.AppIcons
import com.Kolqamo5.Vanmow.core.ui.CarrotPill
import com.Kolqamo5.Vanmow.core.ui.ClayIconButton
import com.Kolqamo5.Vanmow.core.ui.GardenBackdrop
import com.Kolqamo5.Vanmow.core.ui.PrimaryButton
import com.Kolqamo5.Vanmow.core.ui.SecondaryButton
import com.Kolqamo5.Vanmow.core.ui.StatCard

@Composable
fun MenuRoute(
    onPlay: () -> Unit,
    onDailyBonus: () -> Unit,
    onRewards: () -> Unit,
    onCollection: () -> Unit,
    onProgress: () -> Unit,
    onSettings: () -> Unit
) {
    val menuViewModel: MenuViewModel = viewModel(factory = ServiceLocator.viewModelFactory)
    val state by menuViewModel.uiState.collectAsStateWithLifecycle()

    MenuScreen(
        state = state,
        onPlay = onPlay,
        onDailyBonus = onDailyBonus,
        onRewards = onRewards,
        onCollection = onCollection,
        onProgress = onProgress,
        onSettings = onSettings
    )
}

@Composable
fun MenuScreen(
    state: MenuUiState,
    onPlay: () -> Unit,
    onDailyBonus: () -> Unit,
    onRewards: () -> Unit,
    onCollection: () -> Unit,
    onProgress: () -> Unit,
    onSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    val transition = rememberInfiniteTransition(label = "menuLoops")
    val rabbitBob by transition.animateFloat(
        initialValue = -8f,
        targetValue = 8f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "rabbitBob"
    )
    val decorTilt by transition.animateFloat(
        initialValue = -4f,
        targetValue = 4f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "decorTilt"
    )

    GardenBackdrop(
        modifier = modifier,
        overlay = listOf(
            BackgroundBase.copy(alpha = 0.10f),
            BackgroundBase.copy(alpha = 0.55f),
            BackgroundBase.copy(alpha = 0.96f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ClayIconButton(
                    iconRes = R.drawable.icon_home,
                    contentDescription = stringResource(R.string.cd_settings),
                    onClick = onSettings
                )
                CarrotPill(count = state.carrots)
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.sprite_3),
                    contentDescription = null,
                    modifier = Modifier
                        .size(64.dp)
                        .offset(x = (-96).dp, y = (-30).dp)
                        .graphicsLayer {
                            rotationZ = decorTilt
                            alpha = 0.22f
                        },
                    contentScale = ContentScale.Fit
                )
                Image(
                    painter = painterResource(R.drawable.sprite_4),
                    contentDescription = null,
                    modifier = Modifier
                        .size(72.dp)
                        .offset(x = 104.dp, y = 16.dp)
                        .graphicsLayer {
                            rotationZ = -decorTilt
                            alpha = 0.26f
                        },
                    contentScale = ContentScale.Fit
                )
                Image(
                    painter = painterResource(R.drawable.sprite_1),
                    contentDescription = stringResource(R.string.cd_rabbit),
                    modifier = Modifier
                        .size(190.dp)
                        .offset(y = rabbitBob.dp),
                    contentScale = ContentScale.Fit
                )
            }

            MenuSheet(
                state = state,
                onPlay = onPlay,
                onDailyBonus = onDailyBonus,
                onRewards = onRewards,
                onCollection = onCollection,
                onProgress = onProgress
            )
        }
    }
}

@Composable
private fun MenuSheet(
    state: MenuUiState,
    onPlay: () -> Unit,
    onDailyBonus: () -> Unit,
    onRewards: () -> Unit,
    onCollection: () -> Unit,
    onProgress: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(topStart = 36.dp, topEnd = 36.dp)
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .shadow(
                elevation = 18.dp,
                shape = shape,
                ambientColor = BrandPrimary,
                spotColor = BrandPrimary
            ),
        shape = shape,
        color = SurfaceCard.copy(alpha = 0.94f),
        border = BorderStroke(2.dp, SurfaceTint)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 28.dp, vertical = 22.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.app_brand),
                style = MaterialTheme.typography.headlineMedium,
                color = TextPrimary,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.menu_tagline),
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary,
                textAlign = TextAlign.Center,
                maxLines = 2
            )

            if (state.showStats) {
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    StatCard(
                        label = stringResource(R.string.stat_best_run),
                        value = state.bestRun.toString(),
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
            }

            Spacer(modifier = Modifier.height(16.dp))
            PrimaryButton(
                text = stringResource(R.string.play),
                onClick = onPlay,
                iconRes = AppIcons.PlayArrow,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(14.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                SecondaryButton(
                    text = stringResource(R.string.daily_bonus_short),
                    onClick = onDailyBonus,
                    iconRes = AppIcons.Redeem,
                    height = 52.dp,
                    modifier = Modifier.weight(1f)
                )
                SecondaryButton(
                    text = stringResource(R.string.rewards),
                    onClick = onRewards,
                    iconRes = AppIcons.Star,
                    height = 52.dp,
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                SecondaryButton(
                    text = stringResource(R.string.collection),
                    onClick = onCollection,
                    iconRes = AppIcons.Favorite,
                    height = 52.dp,
                    modifier = Modifier.weight(1f)
                )
                SecondaryButton(
                    text = stringResource(R.string.progress),
                    onClick = onProgress,
                    iconRes = AppIcons.Info,
                    height = 52.dp,
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
        }
    }
}
