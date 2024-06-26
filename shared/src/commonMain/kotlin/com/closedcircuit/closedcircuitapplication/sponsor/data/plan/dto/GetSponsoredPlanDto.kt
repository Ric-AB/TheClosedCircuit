package com.closedcircuit.closedcircuitapplication.sponsor.data.plan.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetSponsoredPlanDto(
    val plans: List<FundedPlanPreviewDto>,
    @SerialName("pending_plans")
    val pendingPlan: List<PendingPlanDto>
)
