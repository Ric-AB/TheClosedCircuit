package com.closedcircuit.closedcircuitapplication.sponsor.data.plan

import com.closedcircuit.closedcircuitapplication.common.data.util.ClosedCircuitApiEndpoints.PLAN_BY_FUND_REQUEST_ID
import com.closedcircuit.closedcircuitapplication.core.network.ApiResponse
import com.closedcircuit.closedcircuitapplication.sponsor.data.plan.dto.ApiPlan
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Path

interface NoAuthPlanService {

    @GET(PLAN_BY_FUND_REQUEST_ID)
    suspend fun fetchPlanByFundRequestId(@Path("id") fundRequestId: String): ApiResponse<ApiPlan>
}