package com.closedcircuit.closedcircuitapplication.sponsor.data.plan

import com.closedcircuit.closedcircuitapplication.common.data.util.ClosedCircuitApiEndpoints
import com.closedcircuit.closedcircuitapplication.core.network.ApiResponse
import com.closedcircuit.closedcircuitapplication.sponsor.data.plan.dto.FundedPlanDto
import com.closedcircuit.closedcircuitapplication.sponsor.data.plan.dto.GetSponsoredPlanDto
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Path
import de.jensklingenberg.ktorfit.http.Query

interface PlanService {
    @GET(ClosedCircuitApiEndpoints.GET_FUNDED_PLANS_PREVIEW)
    suspend fun fetchFundedPlans(): ApiResponse<GetSponsoredPlanDto>

    @GET(ClosedCircuitApiEndpoints.GET_FUNDED_PLAN_DETAILS)
    suspend fun fetchFundedPlanDetails(@Path("id") planId: String): ApiResponse<FundedPlanDto>
}