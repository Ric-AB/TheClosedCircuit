package com.closedcircuit.closedcircuitapplication.sponsor.data.step

import com.closedcircuit.closedcircuitapplication.common.data.util.ClosedCircuitApiEndpoints
import com.closedcircuit.closedcircuitapplication.core.network.ApiResponse
import com.closedcircuit.closedcircuitapplication.sponsor.data.step.dto.GetStepProofResponse
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Path

interface StepService {

    @GET(ClosedCircuitApiEndpoints.GET_STEP_PROOFS)
    suspend fun fetchStepProofs(@Path("id") stepId: String): ApiResponse<GetStepProofResponse>
}