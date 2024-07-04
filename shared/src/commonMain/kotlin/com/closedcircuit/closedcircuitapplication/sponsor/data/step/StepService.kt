package com.closedcircuit.closedcircuitapplication.sponsor.data.step

import com.closedcircuit.closedcircuitapplication.common.data.util.ClosedCircuitApiEndpoints
import com.closedcircuit.closedcircuitapplication.core.network.ApiResponse
import com.closedcircuit.closedcircuitapplication.sponsor.data.step.dto.GetStepProofResponse
import com.closedcircuit.closedcircuitapplication.sponsor.data.step.dto.StepApprovalRequest
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Headers
import de.jensklingenberg.ktorfit.http.PATCH
import de.jensklingenberg.ktorfit.http.Path

interface StepService {

    @GET(ClosedCircuitApiEndpoints.GET_STEP_PROOFS)
    suspend fun fetchStepProofs(@Path("id") stepId: String): ApiResponse<GetStepProofResponse>

    @Headers("Content-Type: application/json")
    @PATCH(ClosedCircuitApiEndpoints.APPROVE_STEP)
    suspend fun approveStep(
        @Path("id") id: String,
        @Body request: StepApprovalRequest
    ): ApiResponse<Unit>
}