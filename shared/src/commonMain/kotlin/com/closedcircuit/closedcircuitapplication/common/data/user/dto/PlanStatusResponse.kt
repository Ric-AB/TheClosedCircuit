package com.closedcircuit.closedcircuitapplication.beneficiary.data.user.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlanStatusResponse(
    @SerialName("plan_analytics")
    val planAnalytics: PlanAnalyticsResponse
)
