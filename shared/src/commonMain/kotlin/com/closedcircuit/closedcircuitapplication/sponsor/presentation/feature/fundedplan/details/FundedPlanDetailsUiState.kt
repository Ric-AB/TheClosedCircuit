package com.closedcircuit.closedcircuitapplication.sponsor.presentation.feature.fundedplan.details

import com.closedcircuit.closedcircuitapplication.common.domain.util.TypeWithStringProperties
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
        val amountFunded: String,
        val fundingType: String,
        val fundingLevel: String,
        val fundingDate: String,
        val stepsWithBudgets: ImmutableMap<StepItem, ImmutableList<BudgetItem>>,
        val itemsTotal: String,
        val fundedStepItems: ImmutableList<FundedStepItem>
    ) : FundedPlanDetailsUiState

    data class Error(val message: String) : FundedPlanDetailsUiState
}

data class FundedStepItem(
    val name: String,
    val status: String,
    val budgets: ImmutableList<FundedBudgetItem>
) : TypeWithStringProperties {
    override val properties: List<String>
        get() = listOf(name, status)
}

data class FundedBudgetItem(
    val id: String,
    val name: String,
    val listOfProofs: ImmutableList<String>
)
