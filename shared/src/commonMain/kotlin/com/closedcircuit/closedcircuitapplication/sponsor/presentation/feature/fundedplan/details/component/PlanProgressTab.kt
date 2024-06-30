package com.closedcircuit.closedcircuitapplication.sponsor.presentation.feature.fundedplan.details.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.closedcircuit.closedcircuitapplication.common.presentation.component.table.Table
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.Elevation
import com.closedcircuit.closedcircuitapplication.resources.SharedRes
import com.closedcircuit.closedcircuitapplication.sponsor.presentation.feature.fundedplan.details.FundedStepItem
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun PlanProgressTab(modifier: Modifier, stepItems: ImmutableList<FundedStepItem>) {
    LazyColumn(modifier = modifier) {
        item {
            val headerTitles = persistentListOf(
                stringResource(SharedRes.strings.step_name),
                stringResource(SharedRes.strings.status_label)
            )

            Table(headerTableTitles = headerTitles, data = stepItems)
        }

        item {
            Spacer(Modifier.height(40.dp))
            Text(
                text = stringResource(SharedRes.strings.step_approval_label),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(Modifier.height(20.dp))
            stepItems.forEach {
                StepApprovalCard(it)
                Spacer(Modifier.height(20.dp))
            }
        }
    }
}

@Composable
private fun StepApprovalCard(step: FundedStepItem) {
    OutlinedCard(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp, horizontal = 12.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = step.name,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(Modifier.width(12.dp))
                Text(
                    text = step.status,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            Spacer(Modifier.height(16.dp))
            step.budgets.forEachIndexed { index, fundedBudgetItem ->
                val text = stringResource(SharedRes.strings.budget_item_x_colon_label, index)
                    .plus("\t")
                    .plus(fundedBudgetItem.name)

                Text(text = text, style = MaterialTheme.typography.bodySmall)

                if (index != step.budgets.lastIndex) {
                    Spacer(Modifier.height(16.dp))
                }
            }

            Spacer(Modifier.height(24.dp))
            OutlinedButton(modifier = Modifier.align(Alignment.CenterHorizontally), onClick = {}) {
                Text(stringResource(SharedRes.strings.view_proofs_label))
            }
        }
    }
}