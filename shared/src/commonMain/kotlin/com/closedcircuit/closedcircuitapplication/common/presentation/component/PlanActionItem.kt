package com.closedcircuit.closedcircuitapplication.common.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.Elevation
import com.closedcircuit.closedcircuitapplication.resources.SharedRes
import dev.icerock.moko.resources.compose.stringResource

@Composable
fun BudgetItem(
    modifier: Modifier,
    name: String,
    targetAmount: String,
    amountRaised: String,
    amountRaisedPercent: Float
) {
    Card(
        shape = Shapes().large,
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(Elevation.Level1)
        )
    ) {
        Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 16.dp)) {
            Text(
                text = name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(8.dp))
            Text(text = targetAmount)

            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(SharedRes.strings.funds_raised_progress),
                    style = MaterialTheme.typography.labelSmall
                )

                Text(
                    text = amountRaised,
                    style = MaterialTheme.typography.labelSmall
                )
            }

            Spacer(Modifier.height(4.dp))
            LinearProgressIndicator(
                progress = { amountRaisedPercent },
                modifier = Modifier.fillMaxWidth(),
                strokeCap = StrokeCap.Round,
            )
        }
    }
}