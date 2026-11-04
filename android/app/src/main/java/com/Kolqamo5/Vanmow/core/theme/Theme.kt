package com.Kolqamo5.Vanmow.core.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val GardenColorScheme = lightColorScheme(
    primary = BrandPrimary,
    onPrimary = SurfaceCard,
    primaryContainer = SurfaceAlt,
    onPrimaryContainer = TextPrimary,
    secondary = SecondaryMint,
    onSecondary = TextPrimary,
    secondaryContainer = CharmTileFill,
    onSecondaryContainer = TextPrimary,
    tertiary = AccentGold,
    onTertiary = TextPrimary,
    tertiaryContainer = CarrotTileFill,
    onTertiaryContainer = TextPrimary,
    background = BackgroundBase,
    onBackground = TextPrimary,
    surface = SurfaceCard,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceAlt,
    onSurfaceVariant = TextSecondary,
    outline = SurfaceTint,
    outlineVariant = SurfaceTint,
    error = DangerFox,
    onError = SurfaceCard
)

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = GardenColorScheme,
        typography = AppTypography,
        content = content
    )
}
