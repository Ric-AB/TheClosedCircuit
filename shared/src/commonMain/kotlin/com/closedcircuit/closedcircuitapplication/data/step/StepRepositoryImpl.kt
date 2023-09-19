package com.closedcircuit.closedcircuitapplication.data.step

import com.closedcircuit.closedcircuitapplication.core.network.ApiResponse
import com.closedcircuit.closedcircuitapplication.core.network.mapOnSuccess
import com.closedcircuit.closedcircuitapplication.database.TheClosedCircuitDatabase
import com.closedcircuit.closedcircuitapplication.domain.model.ID
import com.closedcircuit.closedcircuitapplication.domain.step.Step
import com.closedcircuit.closedcircuitapplication.domain.step.StepRepository
import com.closedcircuit.closedcircuitapplication.domain.step.Steps
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.withContext

class StepRepositoryImpl(
    database: TheClosedCircuitDatabase,
    private val stepService: StepService,
    private val ioDispatcher: CoroutineDispatcher,
    defaultDispatcher: CoroutineDispatcher
) : StepRepository {

    override val stepsFlow: Flow<Steps> = flowOf()

    override suspend fun fetchSteps(): ApiResponse<Steps> {
        return withContext(ioDispatcher + NonCancellable) {
            stepService.fetchSteps().mapOnSuccess { response ->
                val apiSteps = response.steps.asStepEntities()
                emptyList<Step>().toImmutableList()
            }
        }
    }

    override suspend fun createStep(): ApiResponse<Step> {
        TODO("Not yet implemented")
    }

    override suspend fun updateStep(): ApiResponse<Step> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteStep(id: ID): ApiResponse<Step> {
        TODO("Not yet implemented")
    }
}