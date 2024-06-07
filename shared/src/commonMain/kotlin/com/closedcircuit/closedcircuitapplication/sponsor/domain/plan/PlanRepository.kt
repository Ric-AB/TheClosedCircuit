package com.closedcircuit.closedcircuitapplication.sponsor.domain.plan

import com.closedcircuit.closedcircuitapplication.common.domain.model.ID
import com.closedcircuit.closedcircuitapplication.core.network.ApiResponse
import com.closedcircuit.closedcircuitapplication.sponsor.data.plan.dto.GetSponsoredPlanDto

interface PlanRepository {

    suspend fun fetchPlanByFundRequestId(fundRequestID: ID): ApiResponse<SponsorPlan>

    suspend fun fetchSponsoredPlans(): ApiResponse<List<DashboardPlan>>
}