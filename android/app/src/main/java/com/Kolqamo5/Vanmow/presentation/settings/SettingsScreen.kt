package com.Kolqamo5.Vanmow.presentation.settings

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
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
import com.Kolqamo5.Vanmow.core.theme.BackgroundBase
import com.Kolqamo5.Vanmow.core.theme.BrandPrimary
import com.Kolqamo5.Vanmow.core.theme.SecondaryMint
import com.Kolqamo5.Vanmow.core.theme.SurfaceAlt
import com.Kolqamo5.Vanmow.core.theme.SurfaceCard
import com.Kolqamo5.Vanmow.core.theme.TextPrimary
import com.Kolqamo5.Vanmow.core.theme.TextTertiary
import com.Kolqamo5.Vanmow.core.ui.AppIcons
import com.Kolqamo5.Vanmow.core.ui.ClayCard
import com.Kolqamo5.Vanmow.core.ui.ScreenTopBar
import com.Kolqamo5.Vanmow.core.ui.SecondaryButton
import com.Kolqamo5.Vanmow.presentation.dialog.ConfirmResetDialog

@Composable
fun SettingsRoute(onBack: () -> Unit) {
    val settingsViewModel: SettingsViewModel = viewModel(factory = ServiceLocator.viewModelFactory)
    val state by settingsViewModel.uiState.collectAsStateWithLifecycle()

    SettingsScreen(
        state = state,
        onSound = settingsViewModel::setSound,
        onHaptics = settingsViewModel::setHaptics,
        onReducedMotion = settingsViewModel::setReducedMotion,
        onAskReset = settingsViewModel::askReset,
        onCancelReset = settingsViewModel::cancelReset,
        onConfirmReset = settingsViewModel::confirmReset,
        onBack = onBack
    )
}

@Composable
fun SettingsScreen(
    state: SettingsUiState,
    onSound: (Boolean) -> Unit,
    onHaptics: (Boolean) -> Unit,
    onReducedMotion: (Boolean) -> Unit,
    onAskReset: () -> Unit,
    onCancelReset: () -> Unit,
    onConfirmReset: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(BackgroundBase, SurfaceAlt)))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
        ) {
            ScreenTopBar(
                title = stringResource(R.string.settings),
                onBack = onBack
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SettingRow(
                    label = stringResource(R.string.sound),
                    checked = state.soundOn,
                    onCheckedChange = onSound
                )
                SettingRow(
                    label = stringResource(R.string.haptics),
                    checked = state.hapticsOn,
                    onCheckedChange = onHaptics
                )
                SettingRow(
                    label = stringResource(R.string.reduced_motion),
                    checked = state.reducedMotion,
                    onCheckedChange = onReducedMotion
                )
                SecondaryButton(
                    text = stringResource(R.string.reset_progress),
                    onClick = onAskReset,
                    iconRes = AppIcons.Refresh,
                    height = 56.dp,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(32.dp))
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(R.drawable.logo),
                    contentDescription = stringResource(R.string.app_brand),
                    modifier = Modifier
                        .size(72.dp)
                        .alpha(0.7f),
                    contentScale = ContentScale.Fit
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.settings_footer),
                    style = MaterialTheme.typography.labelSmall,
                    color = TextTertiary,
                    textAlign = TextAlign.Center
                )
            }
        }
    }

    if (state.confirmResetVisible) {
        ConfirmResetDialog(
            onConfirm = onConfirmReset,
            onDismiss = onCancelReset
        )
    }
}

@Composable
private fun SettingRow(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val stateText = stringResource(if (checked) R.string.state_on else R.string.state_off)
    ClayCard(
        modifier = modifier.fillMaxWidth(),
        radius = 24.dp,
        elevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .padding(horizontal = 18.dp)
                .semantics { stateDescription = stateText },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                color = TextPrimary
            )
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = SurfaceCard,
                    checkedTrackColor = BrandPrimary,
                    checkedBorderColor = BrandPrimary,
                    uncheckedThumbColor = SurfaceCard,
                    uncheckedTrackColor = SecondaryMint.copy(alpha = 0.5f),
                    uncheckedBorderColor = SecondaryMint
                )
            )
        }
    }
}

