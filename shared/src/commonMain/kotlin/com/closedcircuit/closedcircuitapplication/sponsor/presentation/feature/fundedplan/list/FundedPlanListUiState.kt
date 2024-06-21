package com.closedcircuit.closedcircuitapplication.sponsor.presentation.feature.fundedplan.list

import com.closedcircuit.closedcircuitapplication.sponsor.domain.plan.FundedPlanPreview
import kotlinx.collections.immutable.ImmutableList

sealed interface FundedPlanListUiState {
    object Loading : FundedPlanListUiState

    data class Content(val plans: ImmutableList<FundedPlanPreview>) : FundedPlanListUiState

    data class Error(val message: String) : FundedPlanListUiState
}