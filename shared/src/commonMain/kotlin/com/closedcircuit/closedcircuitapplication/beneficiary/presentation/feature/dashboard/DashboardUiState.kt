package com.closedcircuit.closedcircuitapplication.beneficiary.presentation.feature.dashboard

import com.closedcircuit.closedcircuitapplication.beneficiary.domain.donation.Donations
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.sponsor.Sponsor
import com.closedcircuit.closedcircuitapplication.common.domain.plan.Plans
import kotlinx.collections.immutable.ImmutableList

sealed interface DashboardUiState {
    object Loading : DashboardUiState

    object Empty : DashboardUiState

    data class Content(
        val firstName: String,
        val hasVerifiedEmail: Boolean,
        val showAnalytics: Boolean,
        val totalFundsRaised: String,
        val completedPlansCount: Int,
        val ongoingPlansCount: Int,
        val notStartedPlansCount: Int,
        val topSponsors: ImmutableList<Sponsor>?,
        val recentDonation: Donations,
        val recentPlans: Plans
    ) : DashboardUiState

    val getTotalFundsRaised: String?
        get() = (this as? Content)?.totalFundsRaised
}
