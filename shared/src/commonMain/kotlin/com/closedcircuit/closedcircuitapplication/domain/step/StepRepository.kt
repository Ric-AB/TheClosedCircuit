package com.closedcircuit.closedcircuitapplication.domain.step

import com.closedcircuit.closedcircuitapplication.core.network.ApiResponse
import com.closedcircuit.closedcircuitapplication.domain.model.ID
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow

typealias Steps = ImmutableList<Step>

interface StepRepository {

    val stepsFlow: Flow<Steps>

    suspend fun fetchSteps(): ApiResponse<Steps>

    suspend fun createStep(step: Step): ApiResponse<Step>

    suspend fun updateStep(step: Step): ApiResponse<Step>

    suspend fun deleteStep(id: ID): ApiResponse<Step>
}