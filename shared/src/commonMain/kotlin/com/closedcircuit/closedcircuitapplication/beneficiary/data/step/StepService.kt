package com.closedcircuit.closedcircuitapplication.beneficiary.data.step

import com.closedcircuit.closedcircuitapplication.core.network.ApiResponse
import com.closedcircuit.closedcircuitapplication.beneficiary.data.step.dto.ApiStep
import com.closedcircuit.closedcircuitapplication.beneficiary.data.step.dto.SaveStepPayload
import com.closedcircuit.closedcircuitapplication.beneficiary.data.step.dto.GetStepsResponse
import com.closedcircuit.closedcircuitapplication.common.data.util.ClosedCircuitApiEndpoints.STEP
import com.closedcircuit.closedcircuitapplication.common.data.util.ClosedCircuitApiEndpoints.STEPS
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.DELETE
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Headers
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.PUT
import de.jensklingenberg.ktorfit.http.Path

interface StepService {
    @GET(STEPS)
    suspend fun fetchSteps(): ApiResponse<GetStepsResponse>

    @Headers("Content-Type: application/json")
    @POST(STEPS)
    suspend fun createStep(@Body request: SaveStepPayload): ApiResponse<ApiStep>

    @Headers("Content-Type: application/json")
    @PUT(STEP)
    suspend fun updateStep(
        @Path("id") id: String,
        @Body request: SaveStepPayload
    ): ApiResponse<ApiStep>

    @DELETE(STEP)
    suspend fun deleteStep(@Path("id") id: String): ApiResponse<Unit>

    @GET(STEP)
    suspend fun fetchStepById(@Path("id") id: String): ApiResponse<ApiStep>
}