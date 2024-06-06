package com.closedcircuit.closedcircuitapplication.beneficiary.data.plan.dto

import kotlinx.serialization.Serializable

@Serializable
data class GetPlansResponse(
    val plans: List<ApiPlan>
)
