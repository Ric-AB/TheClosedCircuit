package com.closedcircuit.closedcircuitapplication.data.plan

import com.closedcircuit.closedcircuitapplication.core.network.ApiResponse
import com.closedcircuit.closedcircuitapplication.data.plan.dto.ApiPlan
import com.closedcircuit.closedcircuitapplication.data.plan.dto.CreateOrUpdatePlanRequest
import com.closedcircuit.closedcircuitapplication.data.plan.dto.GetPlansResponse
import com.closedcircuit.closedcircuitapplication.util.ClosedCircuitApiEndpoints.CREATE_PLAN
import com.closedcircuit.closedcircuitapplication.util.ClosedCircuitApiEndpoints.PLAN
import com.closedcircuit.closedcircuitapplication.util.ClosedCircuitApiEndpoints.PLANS
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.DELETE
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Headers
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.PUT
import de.jensklingenberg.ktorfit.http.Path
import de.jensklingenberg.ktorfit.http.Query

interface PlanService {

    @GET(PLANS)
    suspend fun getPlans(
        @Query("limit") limit: Int = 100,
        @Query("offset") offSet: Int = 0
    ): ApiResponse<GetPlansResponse>

    @Headers("Content-Type: application/json")
    @POST(CREATE_PLAN)
    suspend fun createPlan(
        @Body request: CreateOrUpdatePlanRequest
    ): ApiResponse<ApiPlan>

    @Headers("Content-Type: application/json")
    @PUT(PLAN)
    suspend fun updateUserPlan(
        @Body request: CreateOrUpdatePlanRequest,
        @Path("id") planId: String
    ): ApiResponse<ApiPlan>

    @DELETE(PLAN)
    suspend fun deletePlan(
        @Path("id") id: String,
    ): ApiResponse<Unit>
}