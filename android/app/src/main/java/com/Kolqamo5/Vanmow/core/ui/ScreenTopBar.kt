package com.Kolqamo5.Vanmow.core.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.Kolqamo5.Vanmow.R
import com.Kolqamo5.Vanmow.core.theme.BrandPrimaryDark
import com.Kolqamo5.Vanmow.core.theme.TextPrimary

@Composable
fun ScreenTopBar(
    title: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(64.dp)
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            color = TextPrimary,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 56.dp)
        )
        ClayIconButton(
            iconRes = AppIcons.ArrowBack,
            contentDescription = stringResource(R.string.cd_back),
            onClick = onBack,
            tint = BrandPrimaryDark,
            modifier = Modifier.align(Alignment.CenterStart)
        )
    }
}
