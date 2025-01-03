package com.closedcircuit.closedcircuitapplication.sponsor.presentation.feature.fundedplan.details

import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.closedcircuit.closedcircuitapplication.common.domain.model.Date
import com.closedcircuit.closedcircuitapplication.common.domain.model.ID
import com.closedcircuit.closedcircuitapplication.common.domain.model.StepStatus
import com.closedcircuit.closedcircuitapplication.common.domain.user.UserRepository
import com.closedcircuit.closedcircuitapplication.common.util.capitalizeFirstChar
import com.closedcircuit.closedcircuitapplication.core.network.onError
import com.closedcircuit.closedcircuitapplication.core.network.onSuccess
import com.closedcircuit.closedcircuitapplication.sponsor.domain.budget.FundedBudget
import com.closedcircuit.closedcircuitapplication.sponsor.domain.plan.FundedPlanPreview
import com.closedcircuit.closedcircuitapplication.sponsor.domain.plan.PlanRepository
import com.closedcircuit.closedcircuitapplication.sponsor.domain.step.FundedStep
import com.closedcircuit.closedcircuitapplication.sponsor.presentation.component.FundingItem
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class FundedPlanDetailsViewModel(
    private val fundedPlanPreview: FundedPlanPreview,
    private val planRepository: PlanRepository,
    private val userRepository: UserRepository
) : ScreenModel {

    val state = mutableStateOf<FundedPlanDetailsUiState>(FundedPlanDetailsUiState.Loading)

    init {
        fetchFundedPlanDetails()
    }

    private fun fetchFundedPlanDetails() {
        screenModelScope.launch {
            planRepository.fetchFundedPlanDetails(fundedPlanPreview.id).onSuccess { fundedPlan ->
                val userId = userRepository.userFlow.firstOrNull()?.id ?: return@onSuccess
                val stepsWithBudget = fundedPlan.steps
                    .associateWith { step ->
                        step.budgets
                            .map { budget -> budget.toFundingItem() }
                            .toImmutableList()
                    }.mapKeys { it.key.toFundingItem() }
                    .toImmutableMap()

                val planID = fundedPlan.id
                val fundedStepItemsWithProofs = fundedPlan.steps
                    .filter { it.status != StepStatus.NOT_COMPLETED }
                    .map { it.toFundedStepItem(userId, planID) }
                    .toImmutableList()

                val fundedStepItems = fundedPlan.steps.map { it.toFundedStepItem(userId, planID) }
                    .toImmutableList()

                state.value = FundedPlanDetailsUiState.Content(
                    planImageUrl = fundedPlan.avatar.value,
                    planDescription = fundedPlan.description,
                    planSector = fundedPlan.sector.capitalizeFirstChar(),
                    planDuration = fundedPlan.duration.value,
                    estimatedCostPrice = fundedPlan.estimatedCostPrice.getFormattedValue(),
                    estimatedSellingPrice = fundedPlan.estimatedSellingPrice.getFormattedValue(),
                    amountFunded = fundedPlanPreview.amountFunded.getFormattedValue(),
                    fundingType = fundedPlanPreview.fundingType.label,
                    fundingLevel = fundedPlanPreview.fundingLevel.label,
                    fundingDate = fundedPlanPreview.fundingDate.format(Date.Format.dd_mmm_yyyy),
                    stepsWithBudgets = stepsWithBudget,
                    itemsTotal = fundedPlan.targetAmount.getFormattedValue(),
                    fundedStepItems = fundedStepItems,
                    fundedStepItemsWithProofs = fundedStepItemsWithProofs,
                    canApprove = fundedPlan.accountabilityPartnerIds.any { it == userId }
                )
            }.onError { _, message ->
                state.value = FundedPlanDetailsUiState.Error(message)
            }
        }
    }

    private fun FundedStep.toFundedStepItem(userId: ID, planID: ID): FundedStepItem {
        return FundedStepItem(
            planID = planID,
            id = id,
            name = name,
            status = status,
            isApprovedByUser = approverIds.any { it == userId },
            budgets = budgets.map { it.toFundedBudgetItem() }.toImmutableList()
        )
    }

    private fun FundedBudget.toFundedBudgetItem(): FundedBudgetItem {
        return FundedBudgetItem(name = name)
    }

    private fun FundedStep.toFundingItem(): FundingItem {
        return FundingItem(
            id = id.value,
            name = name,
            formattedCost = targetFunds.getFormattedValue(),
        )
    }

    private fun FundedBudget.toFundingItem(): FundingItem {
        val fundingStatus = when {
            fundsRaised >= cost -> "Funded"
            !isFunded -> cost.getFormattedValue()
            else -> {
                val amountLeft = cost - fundsRaised
                "${amountLeft.getFormattedValue()} left"
            }
        }

        return FundingItem(
            id = id.value,
            name = name,
            formattedCost = cost.getFormattedValue(),
            fundingStatus = fundingStatus
        )
    }
}