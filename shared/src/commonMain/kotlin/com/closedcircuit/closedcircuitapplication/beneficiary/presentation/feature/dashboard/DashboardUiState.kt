package com.closedcircuit.closedcircuitapplication.beneficiary.presentation.feature.dashboard

import com.closedcircuit.closedcircuitapplication.beneficiary.domain.donation.Donations
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.sponsor.Sponsor
import com.closedcircuit.closedcircuitapplication.common.domain.model.Email
import com.closedcircuit.closedcircuitapplication.common.domain.plan.Plans
import kotlinx.collections.immutable.ImmutableList

sealed interface DashboardUiState {
    object Loading : DashboardUiState

    data class Empty(val hasVerifiedEmail: Boolean, val email: Email) : DashboardUiState

    data class Content(
        val firstName: String,
        val showAnalytics: Boolean,
        val walletBalance: String,
        val completedPlansCount: Int,
        val ongoingPlansCount: Int,
        val notStartedPlansCount: Int,
        val topSponsors: ImmutableList<Sponsor>?,
        val recentDonation: Donations,
        val recentPlans: Plans
    ) : DashboardUiState

    data class Error(val message: String) : DashboardUiState

    val getWalletBalance: String?
        get() = (this as? Content)?.walletBalance
}
