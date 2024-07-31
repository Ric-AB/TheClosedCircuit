package com.closedcircuit.closedcircuitapplication.beneficiary.data.plan

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.closedcircuit.closedcircuitapplication.beneficiary.data.budget.toBudgets
import com.closedcircuit.closedcircuitapplication.beneficiary.data.fundrequest.toFundRequests
import com.closedcircuit.closedcircuitapplication.beneficiary.data.step.toSteps
import com.closedcircuit.closedcircuitapplication.common.domain.budget.BudgetRepository
import com.closedcircuit.closedcircuitapplication.common.domain.fundrequest.FundRequestRepository
import com.closedcircuit.closedcircuitapplication.common.domain.model.ID
import com.closedcircuit.closedcircuitapplication.common.domain.plan.Plan
import com.closedcircuit.closedcircuitapplication.common.domain.plan.PlanRepository
import com.closedcircuit.closedcircuitapplication.common.domain.plan.Plans
import com.closedcircuit.closedcircuitapplication.common.domain.step.StepRepository
import com.closedcircuit.closedcircuitapplication.core.network.ApiResponse
import com.closedcircuit.closedcircuitapplication.core.network.mapOnSuccess
import com.closedcircuit.closedcircuitapplication.database.TheClosedCircuitDatabase
import database.PlanEntity
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class PlanRepositoryImpl(
    database: TheClosedCircuitDatabase,
    private val planService: PlanService,
    private val stepRepository: StepRepository,
    private val budgetRepository: BudgetRepository,
    private val fundRequestRepository: FundRequestRepository,
    private val ioDispatcher: CoroutineDispatcher,
    defaultDispatcher: CoroutineDispatcher
) : PlanRepository {
    private val queries = database.planEntityQueries

    override val plansFlow: Flow<Plans> = queries.getPlanEntities()
        .asFlow()
        .mapToList(defaultDispatcher)
        .map { plans -> plans.toPlans() }

    // todo create use case for this?
    override suspend fun fetchPlans(): ApiResponse<Plans> {
        return withContext(ioDispatcher + NonCancellable) {
            planService.getPlans().mapOnSuccess { response ->
                val apiPlans = response.plans
                queries.transaction {
                    apiPlans.forEach { apiPlan ->
                        saveLocally(apiPlan.asPlanEntity())
                        stepRepository.saveStepLocally(apiPlan.steps.toSteps())
                        budgetRepository.saveBudgetLocally(apiPlan.budgets.toBudgets())
                        fundRequestRepository.saveFundRequestLocally(apiPlan.fundRequests.toFundRequests())
                    }
                }

                apiPlans.toPlans()
            }
        }
    }

    override suspend fun createPlan(plan: Plan): ApiResponse<Plan> {
        return withContext(ioDispatcher + NonCancellable) {
            planService.createPlan(plan.asRequest()).mapOnSuccess { apiPlan ->
                val planEntity = apiPlan.asPlanEntity()
                saveLocally(planEntity)
                planEntity.asPlan()
            }
        }
    }

    override suspend fun updatePlan(plan: Plan): ApiResponse<Plan> {
        return withContext(ioDispatcher + NonCancellable) {
            planService.updateUserPlan(planId = plan.id.value, request = plan.asRequest())
                .mapOnSuccess { apiPlan ->
                    val planEntity = apiPlan.asPlanEntity()
                    saveLocally(planEntity)
                    planEntity.asPlan()
                }
        }
    }

    override suspend fun deletePlan(id: ID): ApiResponse<Plan> {
        val idValue = id.value
        return withContext(ioDispatcher + NonCancellable) {
            planService.deletePlan(idValue).mapOnSuccess {
                val planEntity = queries.getPlanEntityByID(idValue).executeAsOne()
                queries.deletePlanEntity(idValue)
                planEntity.asPlan()
            }
        }
    }

    override fun getRecentPlans(limit: Int): Flow<Plans> {
        return plansFlow.map { it.take(limit).toImmutableList() }
    }

    override fun getPlanByIDAsFlow(id: ID): Flow<Plan?> {
        return queries.getPlanEntityByID(id.value)
            .asFlow()
            .mapToOneOrNull(ioDispatcher)
            .map { it?.asPlan() }
    }

    override suspend fun fetchPlanByID(id: ID): ApiResponse<Plan> {
        return planService.fetchPlanById(id.value).mapOnSuccess { it.asPlan() }
    }

    private fun saveLocally(planEntity: PlanEntity) {
        queries.upsertPlanEntity(planEntity)
    }
}