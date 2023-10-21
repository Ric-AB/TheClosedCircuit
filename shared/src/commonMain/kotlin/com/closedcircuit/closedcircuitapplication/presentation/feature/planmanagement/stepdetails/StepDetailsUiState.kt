package com.closedcircuit.closedcircuitapplication.presentation.feature.planmanagement.stepdetails

import com.closedcircuit.closedcircuitapplication.domain.budget.Budgets
import com.closedcircuit.closedcircuitapplication.domain.step.Step
import kotlinx.collections.immutable.persistentListOf

data class StepDetailsUiState(
    val step: Step,
    val budgets: Budgets = persistentListOf()
) {
    companion object {
        fun init(step: Step): StepDetailsUiState {
            return StepDetailsUiState(step = step)
        }
    }
}
