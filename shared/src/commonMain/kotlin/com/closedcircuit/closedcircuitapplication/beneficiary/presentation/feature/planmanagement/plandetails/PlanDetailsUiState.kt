package com.closedcircuit.closedcircuitapplication.beneficiary.presentation.feature.planmanagement.plandetails

import com.closedcircuit.closedcircuitapplication.common.domain.budget.Budgets
import com.closedcircuit.closedcircuitapplication.common.domain.plan.Plan
import com.closedcircuit.closedcircuitapplication.common.domain.step.Steps
import kotlinx.collections.immutable.persistentListOf

data class PlanDetailsUiState(
    val isLoading: Boolean,
    val canEditPlan: Boolean,
    val plan: Plan,
    val steps: Steps,
    val budgets: Budgets
) {
    companion object {
        fun init(plan: Plan): PlanDetailsUiState {
            return PlanDetailsUiState(
                isLoading = false,
                canEditPlan = !plan.hasReceivedFunds(),
                plan = plan,
                steps = persistentListOf(),
                budgets = persistentListOf()
            )
        }
    }
}

sealed interface PlanDetailsResult {
    object DeleteSuccess : PlanDetailsResult

    data class DeleteError(val message: String) : PlanDetailsResult
}
