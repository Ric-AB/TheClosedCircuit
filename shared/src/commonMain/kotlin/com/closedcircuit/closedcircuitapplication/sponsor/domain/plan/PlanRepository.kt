package com.closedcircuit.closedcircuitapplication.sponsor.domain.plan

import com.closedcircuit.closedcircuitapplication.beneficiary.domain.model.ID
import com.closedcircuit.closedcircuitapplication.core.network.ApiResponse

interface PlanRepository {

    suspend fun fetchPlanByFundRequestId(fundRequestID: ID): ApiResponse<SponsorPlan>
}