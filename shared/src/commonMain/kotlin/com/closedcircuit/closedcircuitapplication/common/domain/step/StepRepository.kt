package com.closedcircuit.closedcircuitapplication.common.domain.step

import com.closedcircuit.closedcircuitapplication.core.network.ApiResponse
import com.closedcircuit.closedcircuitapplication.common.domain.model.ID
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.step.Step
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow

typealias Steps = ImmutableList<Step>

interface StepRepository {

    suspend fun fetchSteps(): ApiResponse<Steps>

    fun saveLocally(step: Step)

    fun saveLocally(steps: List<Step>)

    suspend fun createStep(step: Step): ApiResponse<Step>

    suspend fun updateStep(step: Step): ApiResponse<Step>

    suspend fun deleteStep(id: ID): ApiResponse<Step>

    fun getStepsForPlanAsFlow(planID: ID): Flow<Steps>

    fun getStepByIDAsFlow(id: ID): Flow<Step>
}