package com.Kolqamo5.Vanmow.presentation.gameover

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Kolqamo5.Vanmow.R
import com.Kolqamo5.Vanmow.core.di.ServiceLocator
import com.Kolqamo5.Vanmow.core.navigation.Screen
import com.Kolqamo5.Vanmow.core.theme.AccentGold
import com.Kolqamo5.Vanmow.core.theme.BackgroundBase
import com.Kolqamo5.Vanmow.core.theme.BrandPrimary
import com.Kolqamo5.Vanmow.core.theme.FoxEndingTint
import com.Kolqamo5.Vanmow.core.theme.FullBasketTint
import com.Kolqamo5.Vanmow.core.theme.GameOverlayTop
import com.Kolqamo5.Vanmow.core.theme.SecondaryMint
import com.Kolqamo5.Vanmow.core.theme.TextPrimary
import com.Kolqamo5.Vanmow.core.theme.TextSecondary
import com.Kolqamo5.Vanmow.core.ui.AppIcons
import com.Kolqamo5.Vanmow.core.ui.GardenBackdrop
import com.Kolqamo5.Vanmow.core.ui.PrimaryButton
import com.Kolqamo5.Vanmow.core.ui.SecondaryButton
import com.Kolqamo5.Vanmow.core.ui.StatCard

@Composable
fun GameOverRoute(
    result: Screen.GameOver,
    onPlayAgain: () -> Unit,
    onMenu: () -> Unit
) {
    val gameOverViewModel: GameOverViewModel = viewModel(factory = ServiceLocator.viewModelFactory)
    val state by gameOverViewModel.uiState.collectAsStateWithLifecycle()

    GameOverScreen(
        result = result,
        state = state,
        onPlayAgain = onPlayAgain,
        onMenu = onMenu
    )
}

@Composable
fun GameOverScreen(
    result: Screen.GameOver,
    state: GameOverUiState,
    onPlayAgain: () -> Unit,
    onMenu: () -> Unit,
    modifier: Modifier = Modifier
) {
    val transition = rememberInfiniteTransition(label = "resultLoops")
    val ribbonPulse by transition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "ribbonPulse"
    )

    val overlay = if (result.isFullBasket) {
        listOf(FullBasketTint.copy(alpha = 0.86f), BackgroundBase.copy(alpha = 0.95f))
    } else {
        listOf(GameOverlayTop.copy(alpha = 0.88f), FoxEndingTint.copy(alpha = 0.95f))
    }
    val art = if (result.isFullBasket) R.drawable.sprite_4 else R.drawable.sprite_5
    val visibleCards = listOf(
        result.carrots > 0,
        result.charms > 0,
        state.bestRow > 0
    ).count { it }

    GardenBackdrop(modifier = modifier, overlay = overlay) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(art),
                contentDescription = stringResource(
                    if (result.isFullBasket) R.string.cd_basket else R.string.cd_fox
                ),
                modifier = Modifier.size(150.dp),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(
                    if (result.isFullBasket) R.string.result_full_basket else R.string.result_caught
                ),
                style = MaterialTheme.typography.headlineMedium,
                color = TextPrimary,
                textAlign = TextAlign.Center,
                modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite }
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.result_patches, result.patches),
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )

            if (visibleCards >= 2) {
                Spacer(modifier = Modifier.height(18.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (result.carrots > 0) {
                        StatCard(
                            label = stringResource(R.string.stat_carrots),
                            value = result.carrots.toString(),
                            accent = AccentGold,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    if (result.charms > 0) {
                        StatCard(
                            label = stringResource(R.string.stat_charms),
                            value = result.charms.toString(),
                            accent = SecondaryMint,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    if (state.bestRow > 0) {
                        StatCard(
                            label = stringResource(R.string.stat_best),
                            value = state.bestRow.toString(),
                            accent = BrandPrimary,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            if (result.isNewBest) {
                Spacer(modifier = Modifier.height(16.dp))
                Surface(
                    shape = RoundedCornerShape(18.dp),
                    color = AccentGold,
                    modifier = Modifier.graphicsLayer {
                        scaleX = ribbonPulse
                        scaleY = ribbonPulse
                    }
                ) {
                    Text(
                        text = stringResource(R.string.result_new_best),
                        style = MaterialTheme.typography.labelLarge,
                        color = TextPrimary,
                        modifier = Modifier.padding(horizontal = 18.dp, vertical = 8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(26.dp))
            PrimaryButton(
                text = stringResource(R.string.play_again),
                onClick = onPlayAgain,
                iconRes = AppIcons.PlayArrow,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))
            SecondaryButton(
                text = stringResource(R.string.menu),
                onClick = onMenu,
                iconRes = AppIcons.ArrowBack,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
