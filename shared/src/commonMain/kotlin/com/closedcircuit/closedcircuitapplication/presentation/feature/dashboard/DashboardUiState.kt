package com.closedcircuit.closedcircuitapplication.presentation.feature.dashboard

import com.closedcircuit.closedcircuitapplication.domain.donation.Donations
import com.closedcircuit.closedcircuitapplication.domain.plan.Plans
import com.closedcircuit.closedcircuitapplication.domain.sponsor.Sponsor
import com.closedcircuit.closedcircuitapplication.domain.user.User
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class DashboardUiState(
    val firstName: String? = null,
    val planAnalytics: PlanAnalyticsUIState? = null,
    val topSponsors: ImmutableList<Sponsor>? = null,
    val recentDonation: Donations = persistentListOf(),
    val recentPlans: Plans = persistentListOf()
)

data class PlanAnalyticsUIState(
    val completedPlansCount: Int,
    val ongoingPlansCount: Int,
    val notStartedPlansCount: Int
)
