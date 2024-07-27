package com.closedcircuit.closedcircuitapplication.beneficiary.data.step

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.closedcircuit.closedcircuitapplication.common.domain.model.ID
import com.closedcircuit.closedcircuitapplication.common.domain.step.Step
import com.closedcircuit.closedcircuitapplication.common.domain.step.StepRepository
import com.closedcircuit.closedcircuitapplication.common.domain.step.Steps
import com.closedcircuit.closedcircuitapplication.core.network.ApiResponse
import com.closedcircuit.closedcircuitapplication.core.network.mapOnSuccess
import com.closedcircuit.closedcircuitapplication.database.TheClosedCircuitDatabase
import database.StepEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class StepRepositoryImpl(
    database: TheClosedCircuitDatabase,
    private val stepService: StepService,
    private val ioDispatcher: CoroutineDispatcher,
    private val defaultDispatcher: CoroutineDispatcher
) : StepRepository {
    private val queries = database.stepEntityQueries

    // todo use save locally to avoid duplicates
    override suspend fun fetchSteps(): ApiResponse<Steps> {
        return withContext(ioDispatcher + NonCancellable) {
            stepService.fetchSteps().mapOnSuccess { response ->
                val stepEntities = response.steps.toStepEntities()
                saveLocally(stepEntities)
                stepEntities.toSteps()
            }
        }
    }

    override fun saveStepLocally(steps: List<Step>) {
        queries.transaction {
            steps.forEach {
                saveLocally(it.asEntity())
            }
        }
    }

    override suspend fun createStep(step: Step): ApiResponse<Step> {
        return withContext(ioDispatcher + NonCancellable) {
            stepService.createStep(step.asRequest()).mapOnSuccess { apiStep ->
                val stepEntity = apiStep.asStepEntity()
                saveLocally(stepEntity)
                stepEntity.asStep()
            }
        }
    }

    override suspend fun updateStep(step: Step): ApiResponse<Step> {
        return withContext(ioDispatcher + NonCancellable) {
            stepService.updateStep(id = step.id.value, step.asRequest()).mapOnSuccess { apiStep ->
                val stepEntity = apiStep.asStepEntity()
                saveLocally(stepEntity)
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

    override fun getStepsForPlanAsFlow(planID: ID): Flow<Steps> {
        return queries.getStepEntitiesForPlan(planID.value)
            .asFlow()
            .mapToList(defaultDispatcher)
            .map { it.toSteps() }
    }

    override fun getStepByIDAsFlow(id: ID): Flow<Step?> {
        return queries.getStepEntityByID(id.value)
            .asFlow()
            .mapToOneOrNull(defaultDispatcher)
            .map { it?.asStep() }
    }

    override fun getPrecedingStepAsFlowFor(stepID: ID, planID: ID): Flow<Step?> {
        return queries.getPrecedingStepFor(stepID.value, planID.value)
            .asFlow()
            .mapToOneOrNull(defaultDispatcher)
            .map { it?.asStep() }

    }

    override suspend fun fetchStepByID(id: ID): ApiResponse<Step> {
        return stepService.fetchStepById(id.value).mapOnSuccess {
            it.asStep()
        }
    }

    private fun saveLocally(stepEntity: StepEntity) {
        queries.upsertStepEntity(stepEntity)
    }

    private fun saveLocally(stepEntities: List<StepEntity>) {
        queries.transaction {
            stepEntities.forEach { saveLocally(it) }
        }
    }
}