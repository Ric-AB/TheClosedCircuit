package com.closedcircuit.closedcircuitapplication.common.presentation.component

import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

@Composable
fun AppExtendedFab(
    modifier: Modifier = Modifier,
    autoShrink: Boolean = false,
    expanded: Boolean = true,
    text: @Composable () -> Unit,
    icon: @Composable () -> Unit,
    onClick: () -> Unit
) {
    val expandedState by produceState(expanded) {
        delay(1.seconds)
        value = !autoShrink
    }

    ExtendedFloatingActionButton(
        modifier = modifier,
        onClick = onClick,
        text = text,
        icon = icon,
        expanded = expandedState,
    )
}

@Composable
fun AppExtendedFabWithLoader(
    modifier: Modifier = Modifier,
    showLoader: Boolean,
    autoShrink: Boolean = false,
    text: @Composable () -> Unit,
    icon: @Composable () -> Unit,
    onClick: () -> Unit
) {
    AppExtendedFab(
        modifier = modifier,
        autoShrink = autoShrink,
        text = text,
        expanded = !showLoader,
        icon = {
            if (showLoader) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    strokeCap = StrokeCap.Round,
                    strokeWidth = 3.dp
                )
            } else icon.invoke()
        },
        onClick = onClick
    )
}