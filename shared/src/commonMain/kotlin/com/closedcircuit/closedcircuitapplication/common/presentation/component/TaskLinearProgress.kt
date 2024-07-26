package com.closedcircuit.closedcircuitapplication.common.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun TaskLinearProgress(
    modifier: Modifier = Modifier,
    progress: Float,
    label: String,
    trackColor: Color = ProgressIndicatorDefaults.linearTrackColor
) {
    val usableProgress = remember(progress) {
        if (progress > 1) progress.div(100)
        else progress
    }

    Column(modifier = modifier) {
        LinearProgressIndicator(
            progress = { usableProgress },
            strokeCap = StrokeCap.Round,
            modifier = Modifier.fillMaxWidth(),
            trackColor = trackColor
        )

        Spacer(Modifier.height(8.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}