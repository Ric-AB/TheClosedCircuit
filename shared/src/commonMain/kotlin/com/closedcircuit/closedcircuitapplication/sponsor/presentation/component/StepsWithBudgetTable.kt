package com.closedcircuit.closedcircuitapplication.sponsor.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.primary3
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.primary5
import com.closedcircuit.closedcircuitapplication.common.util.Empty
import com.closedcircuit.closedcircuitapplication.resources.SharedRes
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap

typealias StepItem = FundingItem
typealias BudgetItem = FundingItem

data class FundingItem(
    val id: String,
    val name: String,
    val formattedCost: String,
)

@Composable
fun StepsWithBudgetTable(items: ImmutableMap<StepItem, ImmutableList<BudgetItem>>, total: String) {
    Column {
        val boldFontStyle =
            MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold)
        items.entries.forEachIndexed { stepIndex, entry ->
            val step = entry.key
            val stepNumber = stepIndex + 1
            ListItem(
                label = stringResource(SharedRes.strings.step_x_colon_label, stepNumber),
                title = step.name,
                amount = stringResource(
                    SharedRes.strings.cost_x_label,
                    step.formattedCost
                ),
                backgroundColor = primary5,
                textStyle = boldFontStyle
            )

            val budgets = entry.value
            budgets.forEachIndexed { budgetIndex, budget ->
                val budgetNumber = budgetIndex + 1
                ListItem(
                    label = stringResource(
                        SharedRes.strings.budget_item_x_colon_label,
                        budgetNumber
                    ),
                    title = budget.name,
                    amount = budget.formattedCost,
                    backgroundColor = primary3,
                    textStyle = MaterialTheme.typography.bodySmall
                )
            }
        }

        ListItem(
            label = stringResource(SharedRes.strings.total_label),
            title = String.Empty,
            amount = total,
            backgroundColor = Color.White,
            textStyle = boldFontStyle
        )

        HorizontalDivider(Modifier.fillMaxWidth(), thickness = 1.dp, color = primary5)
    }
}

@Composable
private fun ListItem(
    label: String,
    title: String,
    amount: String,
    backgroundColor: Color,
    textStyle: TextStyle
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = textStyle,
            modifier = Modifier.weight(1f)
        )

        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title,
            style = textStyle,
            modifier = Modifier.weight(1f)
        )

        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = amount,
            style = textStyle,
            modifier = Modifier.weight(1f)
        )
    }
}