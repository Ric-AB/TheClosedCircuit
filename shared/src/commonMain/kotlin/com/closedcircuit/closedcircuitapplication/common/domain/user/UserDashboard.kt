package com.closedcircuit.closedcircuitapplication.beneficiary.domain.user

import com.closedcircuit.closedcircuitapplication.beneficiary.domain.model.Amount
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.sponsor.Sponsor

data class UserDashboard(
    val completedPlansCount: Int,
    val ongoingPlansCount: Int,
    val notStartedPlansCount: Int,
    val totalFundsRaised: Amount,
    val topSponsors: List<Sponsor>
)
