package com.Kolqamo5.Vanmow.core.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.Kolqamo5.Vanmow.core.theme.BrandPrimary
import com.Kolqamo5.Vanmow.core.theme.SurfaceCard
import com.Kolqamo5.Vanmow.core.theme.SurfaceTint

@Composable
fun ClayCard(
    modifier: Modifier = Modifier,
    radius: Dp = 24.dp,
    elevation: Dp = 12.dp,
    fill: Color = SurfaceCard.copy(alpha = 0.9f),
    borderColor: Color = SurfaceTint,
    borderWidth: Dp = 2.dp,
    shadowColor: Color = BrandPrimary,
    content: @Composable BoxScope.() -> Unit
) {
    val shape = RoundedCornerShape(radius)
    Surface(
        modifier = modifier.shadow(
            elevation = elevation,
            shape = shape,
            ambientColor = shadowColor,
            spotColor = shadowColor
        ),
        shape = shape,
        color = fill,
        border = BorderStroke(borderWidth, borderColor)
    ) {
        Box(content = content)
    }
}
