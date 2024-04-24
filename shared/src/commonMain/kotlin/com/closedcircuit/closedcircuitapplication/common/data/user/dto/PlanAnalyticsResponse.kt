package com.closedcircuit.closedcircuitapplication.beneficiary.data.user.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PlanAnalyticsResponse(
    @SerialName("on_going")
    val onGoing: Int,
    @SerialName("completed")
    val completed: Int,
    @SerialName("not_started")
    val notStarted: Int
)
