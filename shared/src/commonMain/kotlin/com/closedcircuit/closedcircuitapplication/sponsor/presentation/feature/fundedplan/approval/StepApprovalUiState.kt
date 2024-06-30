package com.closedcircuit.closedcircuitapplication.sponsor.presentation.feature.fundedplan.approval

import kotlinx.collections.immutable.ImmutableList

sealed interface StepApprovalUiState {
    object Loading : StepApprovalUiState

    data class Content(
        val proofItems: ImmutableList<ProofItem>
    ) : StepApprovalUiState

    data class Error(val message: String) : StepApprovalUiState
}

data class ProofItem(
    val name: String,
    val description: String,
    val isApproved: Boolean,
    val images: ImmutableList<String>,
)