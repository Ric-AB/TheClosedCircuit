package com.closedcircuit.closedcircuitapplication.beneficiary.presentation.feature.planmanagement.stepdetails

import com.closedcircuit.closedcircuitapplication.common.domain.budget.Budgets
import com.closedcircuit.closedcircuitapplication.common.domain.step.Step
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
