package com.Kolqamo5.Vanmow.core.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.Kolqamo5.Vanmow.core.theme.BrandPrimary
import com.Kolqamo5.Vanmow.core.theme.BrandPrimaryDark
import com.Kolqamo5.Vanmow.core.theme.BrandPrimaryLight
import com.Kolqamo5.Vanmow.core.theme.SurfaceCard

@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    iconRes: Int? = null,
    enabled: Boolean = true,
    height: Dp = 64.dp,
    gradient: List<Color> = listOf(BrandPrimaryLight, BrandPrimary, BrandPrimaryDark),
    labelColor: Color = SurfaceCard
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed && enabled) 0.96f else 1f,
        animationSpec = spring(dampingRatio = 0.5f, stiffness = 500f),
        label = "primaryButtonScale"
    )
    val shape = RoundedCornerShape(height / 2)
    val fillColors = if (enabled) gradient else gradient.map { it.copy(alpha = 0.4f) }

    Button(
        onClick = onClick,
        modifier = modifier
            .height(height)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .shadow(
                elevation = if (enabled) 12.dp else 0.dp,
                shape = shape,
                ambientColor = gradient.first(),
                spotColor = gradient.last()
            )
            .clip(shape)
            .background(Brush.horizontalGradient(fillColors)),
        enabled = enabled,
        shape = shape,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = labelColor,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = labelColor.copy(alpha = 0.6f)
        ),
        elevation = null,
        contentPadding = PaddingValues(horizontal = 20.dp),
        interactionSource = interactionSource
    ) {
        if (iconRes != null) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
        }
        Text(
            text = text,
            style = MaterialTheme.typography.titleLarge
        )
    }
}
