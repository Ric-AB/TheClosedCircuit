package com.closedcircuit.closedcircuitapplication.presentation.feature.dashboard

import com.closedcircuit.closedcircuitapplication.domain.plan.Plan
import com.closedcircuit.closedcircuitapplication.domain.user.Sponsor
import com.closedcircuit.closedcircuitapplication.domain.user.User
import com.closedcircuit.closedcircuitapplication.domain.wallet.Wallet

data class DashboardUIState(
    val currentUser: User? = null,
    val wallet: Wallet? = null,
    val planAnalytics: PlanAnalyticsUIState? = null,
    val topSponsors: List<Sponsor>? = null,
    val recentDonation: List<String> = emptyList(),
    val recentPlans: List<Plan> = emptyList()
)

data class PlanAnalyticsUIState(
    val completedPlansCount: Int,
    val ongoingPlansCount: Int,
    val notStartedPlansCount: Int
)
