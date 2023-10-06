package com.closedcircuit.closedcircuitapplication.presentation.feature.planmanagement.plandetails

import com.closedcircuit.closedcircuitapplication.domain.budget.Budgets
import com.closedcircuit.closedcircuitapplication.domain.plan.Plan
import com.closedcircuit.closedcircuitapplication.domain.step.Steps
import kotlinx.collections.immutable.persistentListOf

data class PlanDetailsUIState(
    val plan: Plan,
    val steps: Steps = persistentListOf(),
    val budgets: Budgets = persistentListOf()
) {
    companion object {
        fun init(plan: Plan): PlanDetailsUIState {
            return PlanDetailsUIState(plan)
        }
    }
}
