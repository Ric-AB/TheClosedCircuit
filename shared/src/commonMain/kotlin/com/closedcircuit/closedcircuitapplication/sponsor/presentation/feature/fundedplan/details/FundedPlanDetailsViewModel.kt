package com.closedcircuit.closedcircuitapplication.sponsor.presentation.feature.fundedplan.details

import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.closedcircuit.closedcircuitapplication.common.domain.budget.Budget
import com.closedcircuit.closedcircuitapplication.common.domain.model.ID
import com.closedcircuit.closedcircuitapplication.common.domain.step.Step
import com.closedcircuit.closedcircuitapplication.core.network.onError
import com.closedcircuit.closedcircuitapplication.core.network.onSuccess
import com.closedcircuit.closedcircuitapplication.sponsor.domain.budget.FundedBudget
import com.closedcircuit.closedcircuitapplication.sponsor.domain.plan.FundedPlan
import com.closedcircuit.closedcircuitapplication.sponsor.domain.plan.PlanRepository
import com.closedcircuit.closedcircuitapplication.sponsor.domain.step.FundedStep
import com.closedcircuit.closedcircuitapplication.sponsor.presentation.component.FundingItem
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.coroutines.launch

class FundedPlanDetailsViewModel(
    private val planID: ID,
    private val planRepository: PlanRepository
) : ScreenModel {

    val state = mutableStateOf<FundedPlanDetailsUiState>(FundedPlanDetailsUiState.Loading)

    init {
        fetchFundedPlanDetails()
    }

    private fun fetchFundedPlanDetails() {
        screenModelScope.launch {
            planRepository.fetchFundedPlanDetails(planID).onSuccess { fundedPlan ->
                val stepsWithBudget = fundedPlan.steps
                    .associateWith { step ->
                        step.budgets
                            .map { budget -> budget.toFundingItem() }
                            .toImmutableList()
                    }.mapKeys { it.key.toFundingItem() }
                    .toImmutableMap()

                state.value = FundedPlanDetailsUiState.Content(
                    planImageUrl = fundedPlan.avatar.value,
                    planDescription = fundedPlan.description,
                    planSector = fundedPlan.sector,
                    planDuration = fundedPlan.duration.value,
                    estimatedCostPrice = fundedPlan.estimatedCostPrice.getFormattedValue(),
                    estimatedSellingPrice = fundedPlan.estimatedSellingPrice.getFormattedValue(),
                    stepsWithBudgets = stepsWithBudget,
                    total = fundedPlan.targetAmount.getFormattedValue()
                )
            }.onError { _, message ->
                state.value = FundedPlanDetailsUiState.Error(message)
            }
        }
    }

    private fun FundedStep.toFundingItem(): FundingItem {
        return FundingItem(
            id = id,
            name = name,
            formattedCost = targetFunds.getFormattedValue(),
            cost = targetFunds.value,
            isSelected = false
        )
    }

    private fun FundedBudget.toFundingItem(): FundingItem {
        return FundingItem(
            id = id,
            name = name,
            formattedCost = cost.getFormattedValue(),
            cost = cost.value,
            isSelected = false
        )
    }

}