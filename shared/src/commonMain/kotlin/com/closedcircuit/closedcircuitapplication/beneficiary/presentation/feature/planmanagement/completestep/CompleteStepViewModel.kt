package com.closedcircuit.closedcircuitapplication.beneficiary.presentation.feature.planmanagement.completestep

import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.closedcircuit.closedcircuitapplication.common.domain.budget.Budget
import com.closedcircuit.closedcircuitapplication.common.domain.model.ID
import com.closedcircuit.closedcircuitapplication.common.domain.plan.PlanRepository
import com.closedcircuit.closedcircuitapplication.common.domain.step.StepRepository
import com.closedcircuit.closedcircuitapplication.core.network.onError
import com.closedcircuit.closedcircuitapplication.core.network.onSuccess
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch

class CompleteStepViewModel(
    private val planID: ID,
    private val stepID: ID,
    private val planRepository: PlanRepository,
    private val stepRepository: StepRepository
) : ScreenModel {

    val state = mutableStateOf<CompleteStepUiState>(CompleteStepUiState.Loading)

    init {
        fetchPlan()
    }

    fun onEvent(event: CompleteStepUiEvent) {
        when (event) {
            CompleteStepUiEvent.CompleteStep -> completeStep()
        }
    }

    private fun fetchPlan() {
        screenModelScope.launch {
            planRepository.fetchPlanByID(planID).onSuccess {
                fetchStep(it.accountabilityPartners)
            }.onError { _, message ->
                state.value = CompleteStepUiState.Error(message)
            }
        }
    }

    private suspend fun fetchStep(accountabilityPartners: List<ID>) {
        stepRepository.fetchStepByID(stepID).onSuccess {
            val budgetItems = it.budgets.toBudgetItems(accountabilityPartners)
            state.value = CompleteStepUiState.Content(budgetItems)
        }.onError { _, message ->
            state.value = CompleteStepUiState.Error(message)
        }
    }

    private fun completeStep() {}

    private fun List<Budget>.toBudgetItems(accountabilityPartners: List<ID>): ImmutableList<BudgetItem> {
        return map { budget ->
            val uploadStatus = when {
                budget.isApproved(accountabilityPartners) -> "Approved"
                budget.hasUploadedProof -> "Pending approval"
                else -> "No uploads"
            }


            BudgetItem(
                id = budget.id,
                name = budget.name,
                amount = budget.cost.getFormattedValue(),
                uploadStatus = uploadStatus
            )
        }.toImmutableList()
    }
}