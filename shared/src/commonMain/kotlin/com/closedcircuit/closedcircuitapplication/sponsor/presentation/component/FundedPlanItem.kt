package com.closedcircuit.closedcircuitapplication.sponsor.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.closedcircuit.closedcircuitapplication.common.presentation.component.Avatar
import com.closedcircuit.closedcircuitapplication.common.presentation.component.TaskLinearProgress
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.Elevation
import com.closedcircuit.closedcircuitapplication.resources.SharedRes
import dev.icerock.moko.resources.compose.stringResource

@Composable
fun FundedPlanItem(
    modifier: Modifier = Modifier,
    beneficiaryFullName: String,
    planImageUrl: String,
    planSector: String,
    amountFunded: String,
    fundingType: String,
    fundsRaisedPercent: Double,
    taskCompletedPercent: Double,
    onClick: () -> Unit
) {
    Card(modifier = modifier, onClick = onClick) {
        Column(
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 16.dp)
        ) {
            Row {
                Avatar(imageUrl = planImageUrl, size = DpSize(50.dp, 50.dp))

                Spacer(Modifier.width(12.dp))
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    ItemText(
                        text = getAnnotatedString(
                            label = stringResource(SharedRes.strings.beneficiary_colon_label),
                            value = beneficiaryFullName
                        )
                    )

                    ItemText(
                        text = getAnnotatedString(
                            label = stringResource(SharedRes.strings.plan_sector_colon_label),
                            value = planSector
                        )
                    )

                    ItemText(
                        text = getAnnotatedString(
                            label = stringResource(SharedRes.strings.amount_funded_colon_label),
                            value = amountFunded
                        )
                    )

                    ItemText(
                        text = getAnnotatedString(
                            label = stringResource(SharedRes.strings.funding_type_colon_label),
                            value = fundingType
                        )
                    )
                }
            }

            Spacer(Modifier.height(20.dp))
            println("FUND RAISED PERCENT $fundsRaisedPercent")
            TaskLinearProgress(
                modifier = Modifier.fillMaxWidth(),
                progress = fundsRaisedPercent.div(100).toFloat(),
                label = stringResource(
                    SharedRes.strings.x_percent_funds_raised_label,
                    fundsRaisedPercent.toString()
                ),
                trackColor = MaterialTheme.colorScheme.surfaceColorAtElevation(Elevation.Level2)
            )

            Spacer(Modifier.height(12.dp))
            TaskLinearProgress(
                modifier = Modifier.fillMaxWidth(),
                progress = taskCompletedPercent.div(100).toFloat(),
                label = stringResource(
                    SharedRes.strings.x_percent_tasks_completed_label,
                    taskCompletedPercent.toString()
                ),
                trackColor = MaterialTheme.colorScheme.surfaceColorAtElevation(Elevation.Level2)
            )
        }
    }
}

@Composable
private fun ItemText(text: AnnotatedString) {
    Text(text = text, style = MaterialTheme.typography.bodySmall)
}

private fun getAnnotatedString(label: String, value: String): AnnotatedString {
    return buildAnnotatedString {
        append(label)
        append(" ")
        withStyle(style = SpanStyle(fontWeight = FontWeight.SemiBold)) {
            append(value)
        }
    }
}