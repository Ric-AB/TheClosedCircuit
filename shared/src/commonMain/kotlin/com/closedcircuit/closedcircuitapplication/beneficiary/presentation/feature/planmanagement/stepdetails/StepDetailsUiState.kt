package com.closedcircuit.closedcircuitapplication.beneficiary.presentation.feature.planmanagement.stepdetails

import com.closedcircuit.closedcircuitapplication.beneficiary.domain.budget.Budgets
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.step.Step
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