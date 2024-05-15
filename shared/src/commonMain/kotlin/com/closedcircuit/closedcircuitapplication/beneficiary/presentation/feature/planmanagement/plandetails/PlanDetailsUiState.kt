package com.closedcircuit.closedcircuitapplication.beneficiary.presentation.feature.planmanagement.plandetails

import com.closedcircuit.closedcircuitapplication.beneficiary.domain.budget.Budgets
import com.closedcircuit.closedcircuitapplication.common.domain.plan.Plan
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.step.Steps
import kotlinx.collections.immutable.persistentListOf

data class PlanDetailsUiState(
    val plan: Plan,
    val steps: Steps = persistentListOf(),
    val budgets: Budgets = persistentListOf()
) {
    companion object {
        fun init(plan: Plan): PlanDetailsUiState {
            return PlanDetailsUiState(plan)
        }
    }
}
