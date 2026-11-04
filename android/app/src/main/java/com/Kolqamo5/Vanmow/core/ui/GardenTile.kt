package com.Kolqamo5.Vanmow.core.ui

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.Kolqamo5.Vanmow.R
import com.Kolqamo5.Vanmow.core.theme.AccentGold
import com.Kolqamo5.Vanmow.core.theme.CarrotTileFill
import com.Kolqamo5.Vanmow.core.theme.CharmTileFill
import com.Kolqamo5.Vanmow.core.theme.DangerFox
import com.Kolqamo5.Vanmow.core.theme.FoxTileFill
import com.Kolqamo5.Vanmow.core.theme.SecondaryMint
import com.Kolqamo5.Vanmow.core.theme.SecondaryMintDeep
import com.Kolqamo5.Vanmow.domain.model.PatchKind

@Composable
fun GardenTile(
    kind: PatchKind,
    revealed: Boolean,
    isActive: Boolean,
    dimmed: Boolean,
    size: Dp,
    patchDescription: String,
    state: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    animate: Boolean = true
) {
    val pulseTransition = rememberInfiniteTransition(label = "tilePulse")
    val pulse by pulseTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (isActive && animate && !revealed) 1.03f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "tilePulseValue"
    )
    val flip by animateFloatAsState(
        targetValue = if (revealed) 180f else 0f,
        animationSpec = tween(durationMillis = 300, easing = FastOutSlowInEasing),
        label = "tileFlip"
    )

    val fill = when {
        !revealed -> SecondaryMint
        kind == PatchKind.CARROT -> CarrotTileFill
        kind == PatchKind.CHARM -> CharmTileFill
        else -> FoxTileFill
    }
    val border = when {
        isActive && !revealed -> BorderStroke(3.dp, AccentGold)
        revealed && kind == PatchKind.FOX -> BorderStroke(3.dp, DangerFox)
        else -> BorderStroke(2.dp, SecondaryMintDeep.copy(alpha = 0.5f))
    }
    val sprite = when (kind) {
        PatchKind.CARROT -> R.drawable.sprite_2
        PatchKind.CHARM -> R.drawable.sprite_3
        PatchKind.FOX -> R.drawable.sprite_5
    }

    Surface(
        onClick = onClick,
        modifier = modifier
            .size(size)
            .alpha(if (dimmed) 0.45f else 1f)
            .graphicsLayer {
                scaleX = pulse
                scaleY = pulse
                rotationY = flip
                cameraDistance = 16f * density
            }
            .shadow(
                elevation = if (isActive) 10.dp else 4.dp,
                shape = RoundedCornerShape(20.dp),
                ambientColor = AccentGold,
                spotColor = SecondaryMintDeep
            )
            .semantics {
                this.contentDescription = patchDescription
                stateDescription = state
            },
        enabled = enabled,
        shape = RoundedCornerShape(20.dp),
        color = fill,
        border = border
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            if (flip < 90f) {
                LeafPattern(modifier = Modifier.fillMaxSize())
            } else {
                Image(
                    painter = painterResource(sprite),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(size * 0.18f)
                        .graphicsLayer { rotationY = 180f },
                    contentScale = ContentScale.Fit
                )
            }
        }
    }
}

@Composable
private fun LeafPattern(modifier: Modifier = Modifier) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Box(
            modifier = Modifier
                .fillMaxSize(0.62f)
                .clip(RoundedCornerShape(topStartPercent = 60, bottomEndPercent = 60))
                .background(Color.White.copy(alpha = 0.18f))
        )
        Box(
            modifier = Modifier
                .fillMaxSize(0.38f)
                .clip(RoundedCornerShape(topEndPercent = 60, bottomStartPercent = 60))
                .background(Color.White.copy(alpha = 0.22f))
        )
    }
}
