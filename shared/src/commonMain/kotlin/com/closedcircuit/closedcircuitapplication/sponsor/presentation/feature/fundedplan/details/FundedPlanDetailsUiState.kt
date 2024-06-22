package com.closedcircuit.closedcircuitapplication.sponsor.presentation.feature.fundedplan.details

sealed interface FundedPlanDetailsUiState {
    object Loading : FundedPlanDetailsUiState

    data class Content(
        val planImageUrl: String,
        val planDescription: String,
        val planSector: String,
        val planDuration: Int,
        val estimatedCostPrice: String,
        val estimatedSellingPrice: String,
    ) : FundedPlanDetailsUiState

    data class Error(val message: String) : FundedPlanDetailsUiState
}
