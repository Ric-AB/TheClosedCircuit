package com.closedcircuit.closedcircuitapplication.domain.user

import com.closedcircuit.closedcircuitapplication.domain.model.Amount
import com.closedcircuit.closedcircuitapplication.domain.sponsor.Sponsor

data class UserDashboard(
    val completedPlansCount: Int,
    val ongoingPlansCount: Int,
    val notStartedPlansCount: Int,
    val totalFundsRaised: Amount,
    val topSponsors: List<Sponsor>
)
