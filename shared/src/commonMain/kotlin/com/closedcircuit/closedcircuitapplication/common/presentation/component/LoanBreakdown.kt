package com.closedcircuit.closedcircuitapplication.common.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.closedcircuit.closedcircuitapplication.resources.SharedRes
import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

enum class LoanBreakdownType {
    LOAN, DURATION
}

@Composable
fun LoanBreakdown(
    modifier: Modifier = Modifier,
    type: LoanBreakdownType,
    data: ImmutableList<String>
) {

    OutlinedCard(modifier = modifier) {
        Column(modifier = Modifier.padding(vertical = 24.dp, horizontal = 12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                val labels = getLabels(type)
                labels.forEachIndexed { index, stringResource ->
                    val weight = if (index == labels.lastIndex) 3f else 2f
                    LabelText(
                        text = stringResource(stringResource),
                        modifier = Modifier.weight(weight)
                    )

                    if (index != labels.lastIndex) {
                        Spacer(Modifier.weight(1f))
                    }
                }
            }

            Spacer(Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                data.take(3).forEachIndexed { index, text ->
                    val weight = if (index == data.lastIndex) 3f else 2f
                    ValueText(text = text, modifier = Modifier.weight(weight))

                    if (index != data.lastIndex) {
                        Text(
                            text = if (index == 0) "+" else "=",
                            fontSize = 16.sp,
                            modifier = Modifier.weight(1f),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LabelText(text: String, modifier: Modifier) {
    Text(text = text, modifier = modifier, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
}

@Composable
private fun ValueText(text: String, modifier: Modifier) {
    Text(text = text, modifier = modifier, fontSize = 12.sp, color = Color.Black)
}

private fun getLabels(type: LoanBreakdownType): ImmutableList<StringResource> {
    return when (type) {
        LoanBreakdownType.LOAN -> {
            persistentListOf(
                SharedRes.strings.loan_label,
                SharedRes.strings.interest_label,
                SharedRes.strings.total_repayment_label
            )
        }

        LoanBreakdownType.DURATION -> {
            persistentListOf(
                SharedRes.strings.grace_label,
                SharedRes.strings.repayment_label,
                SharedRes.strings.total_loan_duration_label
            )
        }
    }
}