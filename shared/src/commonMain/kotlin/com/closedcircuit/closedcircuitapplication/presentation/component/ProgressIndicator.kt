package com.closedcircuit.closedcircuitapplication.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp

@Composable
fun ProgressIndicator(
    modifier: Modifier = Modifier,
    progress: Float,
    displayText: String,
) {
    Column(modifier = modifier) {
        LinearProgressIndicator(
            modifier = Modifier.fillMaxWidth(),
            progress = progress,
            strokeCap = StrokeCap.Round
        )

        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = displayText,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            style = MaterialTheme.typography.labelMedium
        )
    }
}