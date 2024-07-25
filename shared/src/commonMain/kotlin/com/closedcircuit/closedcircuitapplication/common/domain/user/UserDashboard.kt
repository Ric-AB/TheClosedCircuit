package com.closedcircuit.closedcircuitapplication.common.domain.user

import com.closedcircuit.closedcircuitapplication.common.domain.model.Amount
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.sponsor.Sponsor
import com.closedcircuit.closedcircuitapplication.common.domain.model.Currency

data class UserDashboard(
    val completedPlansCount: Int,
    val ongoingPlansCount: Int,
    val notStartedPlansCount: Int,
    val totalFundsRaised: Amount,
    val currency: Currency,
    val topSponsors: List<Sponsor>
)
