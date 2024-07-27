package com.closedcircuit.closedcircuitapplication.beneficiary.presentation.feature.planmanagement.stepdetails

import com.closedcircuit.closedcircuitapplication.common.domain.budget.Budgets
import com.closedcircuit.closedcircuitapplication.common.domain.step.Step
import kotlinx.collections.immutable.persistentListOf

data class StepDetailsUiState(
    val isLoading: Boolean,
    val step: Step,
    val canCompleteStep: Boolean,
    val canDeleteStep: Boolean,
    val budgets: Budgets
) {
    companion object {
        fun init(step: Step): StepDetailsUiState {
            return StepDetailsUiState(
                isLoading = false,
                step = step,
                canCompleteStep = false,
                canDeleteStep = false,
                budgets = persistentListOf()
            )
        }
    }
}

sealed interface StepDetailsResult {
    object DeleteSuccess : StepDetailsResult
    data class DeleteError(val message: String) : StepDetailsResult
}
