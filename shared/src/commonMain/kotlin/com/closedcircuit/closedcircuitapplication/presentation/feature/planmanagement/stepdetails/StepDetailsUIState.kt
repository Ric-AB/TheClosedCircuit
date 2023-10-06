package com.closedcircuit.closedcircuitapplication.presentation.feature.planmanagement.stepdetails

import com.closedcircuit.closedcircuitapplication.domain.budget.Budgets
import com.closedcircuit.closedcircuitapplication.domain.step.Step
import kotlinx.collections.immutable.persistentListOf

data class StepDetailsUIState(
    val step: Step,
    val budgets: Budgets = persistentListOf()
) {
    companion object {
        fun init(step: Step): StepDetailsUIState {
            return StepDetailsUIState(step = step)
        }
    }
}
