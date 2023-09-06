package com.closedcircuit.closedcircuitapplication.domain.user

import com.closedcircuit.closedcircuitapplication.domain.model.Price

data class UserDashboard(
    val user: User,
    val completedPlansCount: Int,
    val ongoingPlansCount: Int,
    val notStartedPlansCount: Int,
    val totalFundsRaised: Price,
    val topSponsors: List<Sponsor>
)
