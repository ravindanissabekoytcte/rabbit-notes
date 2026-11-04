package com.Kolqamo5.Vanmow.presentation.game

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Kolqamo5.Vanmow.R
import com.Kolqamo5.Vanmow.core.di.ServiceLocator
import com.Kolqamo5.Vanmow.core.navigation.Screen
import com.Kolqamo5.Vanmow.core.theme.AccentGold
import com.Kolqamo5.Vanmow.core.theme.AccentGoldDeep
import com.Kolqamo5.Vanmow.core.theme.BackgroundBase
import com.Kolqamo5.Vanmow.core.theme.BrandPrimary
import com.Kolqamo5.Vanmow.core.theme.BrandPrimaryDark
import com.Kolqamo5.Vanmow.core.theme.GameOverlayTop
import com.Kolqamo5.Vanmow.core.theme.SecondaryMint
import com.Kolqamo5.Vanmow.core.theme.SurfaceAlt
import com.Kolqamo5.Vanmow.core.theme.SurfaceCard
import com.Kolqamo5.Vanmow.core.theme.SurfaceTint
import com.Kolqamo5.Vanmow.core.theme.TextPrimary
import com.Kolqamo5.Vanmow.core.ui.AppIcons
import com.Kolqamo5.Vanmow.core.ui.CarrotPill
import com.Kolqamo5.Vanmow.core.ui.ClayIconButton
import com.Kolqamo5.Vanmow.core.ui.GardenBackdrop
import com.Kolqamo5.Vanmow.core.ui.GardenTile
import com.Kolqamo5.Vanmow.core.ui.PrimaryButton
import com.Kolqamo5.Vanmow.core.ui.ProgressBarClay
import com.Kolqamo5.Vanmow.domain.model.PatchKind
import com.Kolqamo5.Vanmow.presentation.dialog.PauseDialog
import kotlin.math.floor

private val BOARD_PAD = 10.dp
private val BOARD_BORDER = 3.dp
private val BOARD_FRAME = BOARD_PAD + BOARD_BORDER
private val TILE_GAP = 8.dp
private val BOARD_MAX_W = 380.dp
private val CONTROLS_RESERVE = 136.dp

@Composable
fun GameRoute(
    onGameOver: (Screen.GameOver) -> Unit,
    onMenu: () -> Unit
) {
    val gameViewModel: GameViewModel = viewModel(factory = ServiceLocator.viewModelFactory)
    val state by gameViewModel.uiState.collectAsStateWithLifecycle()

    DisposableEffect(Unit) {
        gameViewModel.attach()
        onDispose { gameViewModel.pause() }
    }

    LaunchedEffect(Unit) {
        gameViewModel.finishedEvents.collect { event ->
            onGameOver(
                Screen.GameOver(
                    carrots = event.carrots,
                    charms = event.charms,
                    patches = event.patches,
                    isFullBasket = event.isFullBasket,
                    isNewBest = event.isNewBest
                )
            )
        }
    }

    GameScreen(
        state = state,
        onPatchTap = gameViewModel::onPatchTap,
        onCollect = gameViewModel::onCollect,
        onPause = gameViewModel::pause,
        onResume = gameViewModel::resume,
        onMenu = onMenu
    )
}

@Composable
fun GameScreen(
    state: GameUiState,
    onPatchTap: (Int) -> Unit,
    onCollect: () -> Unit,
    onPause: () -> Unit,
    onResume: () -> Unit,
    onMenu: () -> Unit,
    modifier: Modifier = Modifier
) {
    var pauseVisible by remember { mutableStateOf(false) }

    val shakeOffset by animateDpAsState(
        targetValue = if (state.shaking) 8.dp else 0.dp,
        animationSpec = tween(durationMillis = 140),
        label = "boardShake"
    )

    GardenBackdrop(
        modifier = modifier,
        overlay = listOf(
            BackgroundBase.copy(alpha = 0.72f),
            GameOverlayTop.copy(alpha = 0.88f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
        ) {
            GameHeader(
                row = state.activeRow.coerceAtMost(state.rows.coerceAtLeast(1)),
                rows = state.rows,
                balance = state.balance,
                onPause = {
                    onPause()
                    pauseVisible = true
                }
            )

            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                val availableWidth = min(maxWidth - 32.dp, BOARD_MAX_W)
                val availableHeight = maxHeight - CONTROLS_RESERVE
                val columns = state.columns.coerceAtLeast(1)
                val rows = state.rows.coerceAtLeast(1)
                val tileFromWidth = floor(
                    (availableWidth - BOARD_FRAME * 2 - TILE_GAP * (columns - 1)).value / columns
                )
                val tileFromHeight = floor(
                    (availableHeight - BOARD_FRAME * 2 - TILE_GAP * (rows - 1)).value / rows
                )
                val tile = kotlin.math.max(
                    24f,
                    kotlin.math.min(tileFromWidth, tileFromHeight)
                ).dp
                val boardWidth = tile * columns + TILE_GAP * (columns - 1) + BOARD_FRAME * 2

                GardenBoard(
                    state = state,
                    tile = tile,
                    boardWidth = boardWidth,
                    onPatchTap = onPatchTap,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .offset(x = shakeOffset)
                )

                FloatingControls(
                    state = state,
                    width = boardWidth,
                    onCollect = onCollect,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 24.dp)
                )
            }
        }
    }

    if (pauseVisible) {
        PauseDialog(
            onResume = {
                pauseVisible = false
                onResume()
            },
            onMenu = {
                pauseVisible = false
                onMenu()
            }
        )
    }
}

@Composable
private fun GameHeader(
    row: Int,
    rows: Int,
    balance: Int,
    onPause: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .height(72.dp),
        shape = RoundedCornerShape(24.dp),
        color = SurfaceCard.copy(alpha = 0.7f),
        border = BorderStroke(2.dp, SurfaceTint)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ClayIconButton(
                iconRes = R.drawable.icon_pause,
                contentDescription = stringResource(R.string.cd_pause),
                onClick = onPause
            )
            Text(
                text = stringResource(R.string.row_progress, row, rows),
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary,
                textAlign = TextAlign.Center,
                modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite }
            )
            CarrotPill(count = balance, height = 40.dp, spriteSize = 20.dp)
        }
    }
}

@Composable
private fun GardenBoard(
    state: GameUiState,
    tile: Dp,
    boardWidth: Dp,
    onPatchTap: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(28.dp)
    Surface(
        modifier = modifier
            .width(boardWidth)
            .shadow(
                elevation = 14.dp,
                shape = shape,
                ambientColor = BrandPrimary,
                spotColor = BrandPrimary
            ),
        shape = shape,
        color = SurfaceCard.copy(alpha = 0.88f),
        border = BorderStroke(BOARD_BORDER, SecondaryMint.copy(alpha = 0.6f))
    ) {
        Box {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .padding(horizontal = 24.dp)
                    .background(Color.White.copy(alpha = 0.9f))
            )
            Column(
                modifier = Modifier.padding(BOARD_PAD),
                verticalArrangement = Arrangement.spacedBy(TILE_GAP)
            ) {
                for (rowIndex in state.board.indices.reversed()) {
                    val rowNumber = rowIndex + 1
                    val isActive = rowNumber == state.activeRow
                    Row(horizontalArrangement = Arrangement.spacedBy(TILE_GAP)) {
                        state.board[rowIndex].forEach { patch ->
                            GardenTile(
                                kind = patch.kind,
                                revealed = patch.revealed,
                                isActive = isActive,
                                dimmed = rowNumber < state.activeRow,
                                size = tile,
                                patchDescription = stringResource(
                                    R.string.cd_patch,
                                    rowNumber,
                                    patch.column + 1
                                ),
                                state = stringResource(
                                    if (!patch.revealed) {
                                        R.string.state_hidden
                                    } else {
                                        when (patch.kind) {
                                            PatchKind.CARROT ->
                                                R.string.state_carrot

                                            PatchKind.CHARM ->
                                                R.string.state_charm

                                            else -> R.string.state_fox
                                        }
                                    }
                                ),
                                enabled = isActive && state.canTap,
                                onClick = { onPatchTap(patch.column) }
                            )
                        }
                    }
                }
            }
            RabbitMarker(
                state = state,
                tile = tile,
                modifier = Modifier.align(Alignment.BottomCenter)
            )
        }
    }
}

@Composable
private fun RabbitMarker(
    state: GameUiState,
    tile: Dp,
    modifier: Modifier = Modifier
) {
    val step = tile + TILE_GAP
    val lift by animateDpAsState(
        targetValue = step * state.clearedRows.toFloat(),
        animationSpec = spring(
            dampingRatio = 0.62f,
            stiffness = Spring.StiffnessMediumLow
        ),
        label = "rabbitHop"
    )
    val squash by animateFloatAsState(
        targetValue = if (state.phase == GamePhase.HOPPING) 0.88f else 1f,
        animationSpec = tween(durationMillis = 120),
        label = "rabbitSquash"
    )

    Image(
        painter = painterResource(R.drawable.sprite_1),
        contentDescription = stringResource(R.string.cd_rabbit),
        modifier = modifier
            .offset(y = -(lift + tile * 0.1f))
            .size(tile * 0.86f)
            .graphicsLayer { scaleY = squash },
        contentScale = ContentScale.Fit
    )
}

@Composable
private fun FloatingControls(
    state: GameUiState,
    width: Dp,
    onCollect: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(30.dp)
    Surface(
        modifier = modifier
            .width(width)
            .height(96.dp)
            .shadow(
                elevation = 16.dp,
                shape = shape,
                ambientColor = BrandPrimary,
                spotColor = BrandPrimary
            ),
        shape = shape,
        color = SurfaceCard.copy(alpha = 0.93f),
        border = BorderStroke(2.dp, SurfaceTint)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Surface(
                    shape = RoundedCornerShape(18.dp),
                    color = SurfaceAlt
                ) {
                    Text(
                        text = stringResource(
                            R.string.multiplier_value,
                            state.multiplier
                        ),
                        style = MaterialTheme.typography.titleMedium,
                        color = BrandPrimaryDark,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(R.drawable.sprite_4),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        contentScale = ContentScale.Fit
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    ProgressBarClay(
                        fraction = state.basketFraction,
                        accent = AccentGold,
                        accentEnd = AccentGoldDeep,
                        barHeight = 10.dp,
                        modifier = Modifier.width(54.dp)
                    )
                }
            }
            PrimaryButton(
                text = stringResource(R.string.collect),
                onClick = onCollect,
                iconRes = AppIcons.Basket,
                enabled = state.canCollect,
                height = 56.dp,
                gradient = listOf(AccentGold, AccentGoldDeep),
                labelColor = TextPrimary,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

