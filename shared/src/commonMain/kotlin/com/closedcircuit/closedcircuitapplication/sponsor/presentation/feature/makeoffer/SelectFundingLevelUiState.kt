package com.closedcircuit.closedcircuitapplication.sponsor.presentation.feature.makeoffer

import com.closedcircuit.closedcircuitapplication.beneficiary.domain.model.ID


sealed interface PlanSummaryUiState {
    object Loading : PlanSummaryUiState

    data class Content(
        val businessSector: String,
        val planDuration: String,
        val estimatedCostPrice: String,
        val estimatedSellingPrice: String,
        val planImage: String,
        val planDescription: String,
        val estimatedProfitPercent: String,
        val estimatedProfitFraction: Float
    ) : PlanSummaryUiState

    data class Error(val message: String) : PlanSummaryUiState
}

data class SelectFundingLevelUiState(
    val fundingLevel: FundingLevel? = null,
)

data class FundingItem(
    val id: ID,
    val name: String,
    val cost: String,
    val isSelected: Boolean
)