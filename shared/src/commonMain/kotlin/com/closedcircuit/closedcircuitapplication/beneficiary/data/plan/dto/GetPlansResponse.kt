package com.closedcircuit.closedcircuitapplication.beneficiary.data.plan.dto

import com.closedcircuit.closedcircuitapplication.common.data.plan.dto.ApiPlan
import kotlinx.serialization.Serializable

@Serializable
data class GetPlansResponse(
    val plans: List<ApiPlan>
)
