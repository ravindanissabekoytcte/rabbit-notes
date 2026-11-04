package com.Kolqamo5.Vanmow.core.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.Kolqamo5.Vanmow.R
import com.Kolqamo5.Vanmow.core.theme.AccentGold
import com.Kolqamo5.Vanmow.core.theme.SurfaceCard
import com.Kolqamo5.Vanmow.core.theme.TextPrimary

@Composable
fun CarrotPill(
    count: Int,
    modifier: Modifier = Modifier,
    height: Dp = 44.dp,
    spriteSize: Dp = 22.dp
) {
    val pulse by animateFloatAsState(
        targetValue = count.toFloat(),
        animationSpec = spring(dampingRatio = 0.55f, stiffness = 420f),
        label = "carrotPulse"
    )
    val scale = 1f + (0.12f * kotlin.math.min(1f, kotlin.math.abs(count - pulse) / 6f))

    Surface(
        modifier = modifier
            .height(height)
            .defaultMinSize(minWidth = 72.dp)
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(height / 2),
                ambientColor = AccentGold,
                spotColor = AccentGold
            ),
        shape = RoundedCornerShape(height / 2),
        color = SurfaceCard.copy(alpha = 0.86f),
        border = BorderStroke(2.dp, AccentGold.copy(alpha = 0.55f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.sprite_2),
                contentDescription = stringResource(R.string.cd_carrot),
                modifier = Modifier
                    .size(spriteSize)
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                    },
                contentScale = ContentScale.Fit
            )
            Text(
                text = count.toString(),
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary,
                modifier = Modifier.semantics { liveRegion = LiveRegionMode.Polite }
            )
        }
    }
}
