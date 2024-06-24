package com.closedcircuit.closedcircuitapplication.sponsor.presentation.feature.fundedplan.details

import com.closedcircuit.closedcircuitapplication.sponsor.presentation.component.BudgetItem
import com.closedcircuit.closedcircuitapplication.sponsor.presentation.component.StepItem
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap

sealed interface FundedPlanDetailsUiState {
    object Loading : FundedPlanDetailsUiState

    data class Content(
        val planImageUrl: String,
        val planDescription: String,
        val planSector: String,
        val planDuration: Int,
        val estimatedCostPrice: String,
        val estimatedSellingPrice: String,
        val stepsWithBudgets: ImmutableMap<StepItem, ImmutableList<BudgetItem>>,
        val total: String
    ) : FundedPlanDetailsUiState

    data class Error(val message: String) : FundedPlanDetailsUiState
}
