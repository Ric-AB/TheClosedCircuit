package com.closedcircuit.closedcircuitapplication.presentation.feature.planmanagement.plandetails

import com.closedcircuit.closedcircuitapplication.domain.plan.Plan

data class PlanDetailsUIState(
    val plan: Plan
) {
    companion object {
        fun init(plan: Plan): PlanDetailsUIState {
            return PlanDetailsUIState(plan)
        }
    }
}
