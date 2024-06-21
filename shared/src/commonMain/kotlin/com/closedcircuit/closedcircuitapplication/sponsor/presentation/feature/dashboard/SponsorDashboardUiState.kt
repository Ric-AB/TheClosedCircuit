package com.closedcircuit.closedcircuitapplication.sponsor.presentation.feature.dashboard

import com.closedcircuit.closedcircuitapplication.sponsor.domain.plan.FundedPlanPreview
import kotlinx.collections.immutable.ImmutableList

sealed interface SponsorDashboardUiState {
    object Loading : SponsorDashboardUiState
    object Empty : SponsorDashboardUiState
    data class Error(val message: String) : SponsorDashboardUiState
    data class Content(
        val userFirstName: String,
        val plans: ImmutableList<FundedPlanPreview>
    ) : SponsorDashboardUiState
}
