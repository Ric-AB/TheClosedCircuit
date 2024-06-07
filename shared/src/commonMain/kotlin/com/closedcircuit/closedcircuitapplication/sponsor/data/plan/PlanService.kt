package com.closedcircuit.closedcircuitapplication.sponsor.data.plan

import com.closedcircuit.closedcircuitapplication.common.data.util.ClosedCircuitApiEndpoints
import com.closedcircuit.closedcircuitapplication.core.network.ApiResponse
import com.closedcircuit.closedcircuitapplication.sponsor.data.plan.dto.GetSponsoredPlanDto
import de.jensklingenberg.ktorfit.http.GET

interface PlanService {
    @GET(ClosedCircuitApiEndpoints.GET_DASHBOARD_PLANS)
    suspend fun fetchSponsoredPlans(): ApiResponse<GetSponsoredPlanDto>
}