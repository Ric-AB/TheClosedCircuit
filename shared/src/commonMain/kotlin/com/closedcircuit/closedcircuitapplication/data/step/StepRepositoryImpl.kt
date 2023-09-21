package com.closedcircuit.closedcircuitapplication.data.step

import com.closedcircuit.closedcircuitapplication.core.network.ApiResponse
import com.closedcircuit.closedcircuitapplication.core.network.mapOnSuccess
import com.closedcircuit.closedcircuitapplication.database.TheClosedCircuitDatabase
import com.closedcircuit.closedcircuitapplication.domain.model.ID
import com.closedcircuit.closedcircuitapplication.domain.step.Step
import com.closedcircuit.closedcircuitapplication.domain.step.StepRepository
import com.closedcircuit.closedcircuitapplication.domain.step.Steps
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
    private val queries = database.stepEntityQueries
    override val stepsFlow: Flow<Steps> = flowOf()

    override suspend fun fetchSteps(): ApiResponse<Steps> {
        return withContext(ioDispatcher + NonCancellable) {
            stepService.fetchSteps().mapOnSuccess { response ->
                val stepEntities = response.steps.asStepEntities()
                queries.transaction {
                    stepEntities.forEach { stepEntity ->
                        queries.upsertStepEntity(stepEntity)
                    }
                }

                stepEntities.asSteps()
            }
        }
    }

    override suspend fun createStep(step: Step): ApiResponse<Step> {
        return withContext(ioDispatcher + NonCancellable) {
            stepService.createStep(step.asRequest()).mapOnSuccess { apiStep ->
                val stepEntity = apiStep.asStepEntity()
                queries.upsertStepEntity(stepEntity)
                stepEntity.asStep()
            }
        }
    }

    override suspend fun updateStep(step: Step): ApiResponse<Step> {
        return withContext(ioDispatcher + NonCancellable) {
            stepService.updateStep(id = step.id.value, step.asRequest()).mapOnSuccess { apiStep ->
                val stepEntity = apiStep.asStepEntity()
                queries.upsertStepEntity(stepEntity)
                stepEntity.asStep()
            }
        }
    }

    override suspend fun deleteStep(id: ID): ApiResponse<Step> {
        val idValue = id.value
        return withContext(ioDispatcher + NonCancellable) {
            stepService.deleteStep(idValue).mapOnSuccess {
                val stepEntity = queries.getStepEntityByID(idValue).executeAsOne()
                queries.deleteStepEntity(idValue)
                stepEntity.asStep()
            }
        }
    }
}