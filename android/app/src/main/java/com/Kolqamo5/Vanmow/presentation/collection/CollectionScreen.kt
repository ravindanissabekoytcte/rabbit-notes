package com.Kolqamo5.Vanmow.presentation.collection

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Kolqamo5.Vanmow.R
import com.Kolqamo5.Vanmow.core.di.ServiceLocator
import com.Kolqamo5.Vanmow.core.theme.BackgroundBase
import com.Kolqamo5.Vanmow.core.theme.CharmTileFill
import com.Kolqamo5.Vanmow.core.theme.SecondaryMint
import com.Kolqamo5.Vanmow.core.theme.SecondaryMintDeep
import com.Kolqamo5.Vanmow.core.theme.SurfaceAlt
import com.Kolqamo5.Vanmow.core.theme.TextSecondary
import com.Kolqamo5.Vanmow.core.theme.TextTertiary
import com.Kolqamo5.Vanmow.core.ui.AppIcons
import com.Kolqamo5.Vanmow.core.ui.ProgressBarClay
import com.Kolqamo5.Vanmow.core.ui.ScreenTopBar
import com.Kolqamo5.Vanmow.presentation.dialog.CharmDialog

@Composable
fun CollectionRoute(onBack: () -> Unit) {
    val collectionViewModel: CollectionViewModel =
        viewModel(factory = ServiceLocator.viewModelFactory)
    val state by collectionViewModel.uiState.collectAsStateWithLifecycle()

    CollectionScreen(
        state = state,
        onSelect = collectionViewModel::select,
        onDismiss = collectionViewModel::dismiss,
        onBack = onBack
    )
}

@Composable
fun CollectionScreen(
    state: CollectionUiState,
    onSelect: (Int) -> Unit,
    onDismiss: () -> Unit,
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
                title = stringResource(R.string.collection),
                onBack = onBack
            )
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Text(
                    text = stringResource(
                        R.string.charms_found,
                        state.ownedCount,
                        state.totalCount
                    ),
                    style = MaterialTheme.typography.labelSmall,
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.height(8.dp))
                ProgressBarClay(
                    fraction = state.fraction,
                    accent = SecondaryMint,
                    accentEnd = SecondaryMintDeep,
                    barHeight = 8.dp
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(state.entries, key = { it.charm.id }) { entry ->
                    CharmTile(entry = entry, onSelect = onSelect)
                }
            }
        }
    }

    val selected = state.selected
    if (selected != null) {
        CharmDialog(charm = selected, onDismiss = onDismiss)
    }
}

@Composable
private fun CharmTile(
    entry: CharmEntry,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val stateText = stringResource(
        if (entry.owned) R.string.state_owned else R.string.state_locked
    )
    Surface(
        onClick = { onSelect(entry.charm.id) },
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .semantics { stateDescription = stateText },
        enabled = entry.owned,
        shape = androidx.compose.foundation.shape.RoundedCornerShape(24.dp),
        color = if (entry.owned) CharmTileFill else SurfaceAlt
    ) {
        Box(contentAlignment = Alignment.Center) {
            Image(
                painter = painterResource(R.drawable.sprite_3),
                contentDescription = stringResource(entry.charm.nameRes),
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize()
                    .alpha(if (entry.owned) 1f else 0.18f),
                contentScale = ContentScale.Fit
            )
            if (!entry.owned) {
                Icon(
                    painter = painterResource(AppIcons.Lock),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                    tint = TextTertiary
                )
            }
        }
    }
}
