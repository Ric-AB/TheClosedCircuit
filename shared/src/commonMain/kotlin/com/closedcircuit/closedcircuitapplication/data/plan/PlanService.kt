package com.closedcircuit.closedcircuitapplication.data.plan

import com.closedcircuit.closedcircuitapplication.core.network.ApiResponse
import com.closedcircuit.closedcircuitapplication.data.plan.dto.GetPlansResponse
import com.closedcircuit.closedcircuitapplication.data.util.ClosedCircuitApiEndpoints.PLANS
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Query

interface PlanService {

    @GET(PLANS)
    suspend fun getPlans(
        @Query("limit") limit: Int = 100,
        @Query("offset") offSet: Int = 0
    ): ApiResponse<GetPlansResponse>
}