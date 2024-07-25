package com.closedcircuit.closedcircuitapplication.common.data.user.dto

import com.closedcircuit.closedcircuitapplication.beneficiary.data.user.dto.PlanStatusResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDashboardResponse(
    @SerialName("total_funds_raised")
    val totalFundsRaised: String,
    @SerialName("plan_status")
    val planStatus: PlanStatusResponse,
    @SerialName("top_sponsors")
    val topSponsors: List<SponsorResponse>,
    val currency: String,
)
