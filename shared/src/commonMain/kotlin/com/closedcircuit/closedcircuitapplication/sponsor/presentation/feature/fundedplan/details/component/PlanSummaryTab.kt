package com.closedcircuit.closedcircuitapplication.sponsor.presentation.feature.fundedplan.details.component

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.closedcircuit.closedcircuitapplication.sponsor.presentation.component.BudgetItem
import com.closedcircuit.closedcircuitapplication.sponsor.presentation.component.StepItem
import com.closedcircuit.closedcircuitapplication.sponsor.presentation.component.StepsWithBudgetTable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap

@Composable
fun PlanSummaryTab(
    modifier: Modifier = Modifier,
    items: ImmutableMap<StepItem, ImmutableList<BudgetItem>>,
    total: String
) {
    LazyColumn(modifier) {
        item {
            StepsWithBudgetTable(
                items = items,
                total = total
            )
        }
    }
}