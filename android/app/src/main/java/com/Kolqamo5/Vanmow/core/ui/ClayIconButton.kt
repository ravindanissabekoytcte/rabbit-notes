package com.Kolqamo5.Vanmow.core.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.Kolqamo5.Vanmow.core.theme.BrandPrimary
import com.Kolqamo5.Vanmow.core.theme.SurfaceCard
import com.Kolqamo5.Vanmow.core.theme.SurfaceTint

@Composable
fun ClayIconButton(
    iconRes: Int,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: Dp = 48.dp,
    iconSize: Dp = 24.dp,
    tint: Color? = null,
    fill: Color = SurfaceCard.copy(alpha = 0.88f),
    borderColor: Color = SurfaceTint
) {
    Surface(
        modifier = modifier
            .size(size)
            .shadow(
                elevation = 8.dp,
                shape = CircleShape,
                ambientColor = BrandPrimary,
                spotColor = BrandPrimary
            ),
        shape = CircleShape,
        color = fill,
        border = BorderStroke(2.dp, borderColor)
    ) {
        IconButton(
            onClick = onClick,
            modifier = Modifier.size(size)
        ) {
            Box(contentAlignment = Alignment.Center) {
                if (tint == null) {
                    Image(
                        painter = painterResource(iconRes),
                        contentDescription = contentDescription,
                        modifier = Modifier.size(iconSize),
                        contentScale = ContentScale.Fit
                    )
                } else {
                    Icon(
                        painter = painterResource(iconRes),
                        contentDescription = contentDescription,
                        modifier = Modifier.size(iconSize),
                        tint = tint
                    )
                }
            }
        }
    }
}
