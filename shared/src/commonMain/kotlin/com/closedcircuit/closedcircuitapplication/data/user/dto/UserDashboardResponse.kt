package com.closedcircuit.closedcircuitapplication.data.user.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDashboardResponse(
    @SerialName("total_funds_raised")
    val totalFundsRaised: String,
    @SerialName("plan_status")
    val planStatus: PlanStatusResponse,
    @SerialName("top_sponsors")
    val topSponsors: List<SponsorResponse>
)
