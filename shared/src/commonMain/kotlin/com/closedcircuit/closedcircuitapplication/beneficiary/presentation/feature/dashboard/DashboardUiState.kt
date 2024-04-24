package com.closedcircuit.closedcircuitapplication.beneficiary.presentation.feature.dashboard

import com.closedcircuit.closedcircuitapplication.beneficiary.domain.donation.Donations
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.plan.Plans
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.sponsor.Sponsor
import kotlinx.collections.immutable.ImmutableList

sealed interface DashboardUiState {
    object Loading : DashboardUiState

    object Empty: DashboardUiState

    data class Content(
        val firstName: String,
        val showAnalytics: Boolean,
        val completedPlansCount: Int,
        val ongoingPlansCount: Int,
        val notStartedPlansCount: Int,
        val topSponsors: ImmutableList<Sponsor>?,
        val recentDonation: Donations,
        val recentPlans: Plans
    ) : DashboardUiState
}
