package com.closedcircuit.closedcircuitapplication.common.domain.step

import com.closedcircuit.closedcircuitapplication.common.domain.model.ID
import com.closedcircuit.closedcircuitapplication.core.network.ApiResponse
import kotlinx.coroutines.flow.Flow

typealias Steps = List<Step>

interface StepRepository {

    suspend fun fetchSteps(): ApiResponse<Steps>

    fun saveStepLocally(steps: List<Step>)

    suspend fun createStep(step: Step): ApiResponse<Step>

    suspend fun updateStep(step: Step): ApiResponse<Step>

    suspend fun deleteStep(id: ID): ApiResponse<Step>

    fun deleteStepsLocally(steps: Steps)

    fun getStepsForPlanAsFlow(planID: ID): Flow<Steps>

    fun getStepByIDAsFlow(id: ID): Flow<Step?>

    fun getPrecedingStepAsFlowFor(stepID: ID, planID: ID): Flow<Step?>

    suspend fun fetchStepByID(id: ID): ApiResponse<Step>
}