package com.closedcircuit.closedcircuitapplication.beneficiary.presentation.feature.planmanagement.completestep

import kotlinx.collections.immutable.ImmutableList

sealed interface CompleteStepUiState {
    object Loading : CompleteStepUiState
    data class Content(
        val budgetItems: ImmutableList<BudgetItem>
    ) : CompleteStepUiState

    data class Error(val message: String)
}

data class BudgetItem(
    val name: String,
    val amount: String,
    val uploadStatus: String
)