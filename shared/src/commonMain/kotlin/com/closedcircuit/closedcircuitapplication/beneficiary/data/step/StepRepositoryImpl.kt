package com.closedcircuit.closedcircuitapplication.beneficiary.data.step

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.closedcircuit.closedcircuitapplication.common.domain.model.Amount
import com.closedcircuit.closedcircuitapplication.common.domain.model.Currency
import com.closedcircuit.closedcircuitapplication.common.domain.model.Date
import com.closedcircuit.closedcircuitapplication.common.domain.model.ID
import com.closedcircuit.closedcircuitapplication.common.domain.model.StepStatus
import com.closedcircuit.closedcircuitapplication.common.domain.model.TaskDuration
import com.closedcircuit.closedcircuitapplication.common.domain.step.Step
import com.closedcircuit.closedcircuitapplication.common.domain.step.StepRepository
import com.closedcircuit.closedcircuitapplication.common.domain.step.Steps
import com.closedcircuit.closedcircuitapplication.common.util.orZero
import com.closedcircuit.closedcircuitapplication.core.network.ApiResponse
import com.closedcircuit.closedcircuitapplication.core.network.mapOnSuccess
import com.closedcircuit.closedcircuitapplication.database.TheClosedCircuitDatabase
import database.StepEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.Flow
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
            queries.getStepEntityByID(idValue)
            stepService.deleteStep(idValue).mapOnSuccess {
                val step = queries.getStepEntityByID(id = idValue, mapper = ::mapToStep)
                    .executeAsOne()

                deleteLocally(idValue)
                step
            }
        }
    }

    override fun deleteStepsLocally(steps: Steps) {
        queries.transaction {
            steps.forEach { deleteLocally(it.id.value) }
        }
    }

    override fun getStepsForPlanAsFlow(planID: ID): Flow<Steps> {
        return queries.getStepEntitiesForPlan(planID = planID.value, mapper = ::mapToStep)
            .asFlow()
            .mapToList(defaultDispatcher)
    }

    override fun getStepByIDAsFlow(stepID: ID): Flow<Step?> {
        return queries.getStepEntityByID(
            id = stepID.value,
            mapper = ::mapToStep
        ).asFlow()
            .mapToOneOrNull(defaultDispatcher)
    }

    override fun getPrecedingStepAsFlowFor(stepID: ID, planID: ID): Flow<Step?> {
        return queries.getPrecedingStepFor(
            id = stepID.value,
            planID = planID.value,
            mapper = ::mapToStep
        ).asFlow()
            .mapToOneOrNull(defaultDispatcher)
    }

    override suspend fun fetchStepByID(id: ID): ApiResponse<Step> {
        return stepService.fetchStepById(id.value).mapOnSuccess {
            it.asStep()
        }
    }

    override suspend fun clear() {
        queries.deleteAll()
    }

    private fun mapToStep(
        id: String,
        name: String,
        description: String,
        duration: Long,
        targetFunds: Double?,
        totalFundsRaised: Double,
        currency: String,
        planID: String,
        userID: String,
        isSponsored: Boolean,
        status: String,
        createdAt: String,
        updatedAt: String,
    ): Step {
        val currencyObj = Currency(currency)
        return Step(
            planID = ID(planID),
            userID = ID(userID),
            id = ID(id),
            name = name,
            description = description,
            duration = TaskDuration(duration),
            targetFunds = Amount(targetFunds.orZero(), currencyObj),
            totalFundsRaised = Amount(totalFundsRaised, currencyObj),
            currency = currencyObj,
            isSponsored = isSponsored,
            status = StepStatus.fromText(status),
            createdAt = Date(createdAt),
            updatedAt = Date(updatedAt)
        )
    }

    private fun deleteLocally(id: String) {
        queries.deleteStepEntity(id)
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