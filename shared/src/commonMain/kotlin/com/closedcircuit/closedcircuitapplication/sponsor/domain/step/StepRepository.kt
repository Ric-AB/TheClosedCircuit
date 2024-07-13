package com.closedcircuit.closedcircuitapplication.sponsor.domain.step

import com.closedcircuit.closedcircuitapplication.common.domain.model.ID
import com.closedcircuit.closedcircuitapplication.core.network.ApiResponse
import com.closedcircuit.closedcircuitapplication.common.domain.model.Proof

interface StepRepository {

    suspend fun fetchStepProofs(stepId: ID): ApiResponse<List<Proof>>

    suspend fun approveStep(stepId: ID): ApiResponse<Unit>
}