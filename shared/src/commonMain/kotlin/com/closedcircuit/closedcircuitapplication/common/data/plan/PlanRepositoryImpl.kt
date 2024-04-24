package com.closedcircuit.closedcircuitapplication.beneficiary.data.plan

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import com.closedcircuit.closedcircuitapplication.core.network.ApiResponse
import com.closedcircuit.closedcircuitapplication.core.network.mapOnSuccess
import com.closedcircuit.closedcircuitapplication.beneficiary.data.budget.toBudgets
import com.closedcircuit.closedcircuitapplication.beneficiary.data.step.toSteps
import com.closedcircuit.closedcircuitapplication.database.TheClosedCircuitDatabase
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.budget.BudgetRepository
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.model.ID
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.plan.Plan
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.plan.PlanRepository
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.plan.Plans
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.step.StepRepository
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
    private val ioDispatcher: CoroutineDispatcher,
    defaultDispatcher: CoroutineDispatcher
) : PlanRepository {
    private val queries = database.planEntityQueries

    override val plansFlow: Flow<Plans> = queries.getPlanEntities()
        .asFlow()
        .mapToList(defaultDispatcher)
        .map { plans -> plans.asPlans() }

    override suspend fun fetchPlans(): ApiResponse<Plans> {
        return withContext(ioDispatcher + NonCancellable) {
            planService.getPlans().mapOnSuccess { response ->
                val apiPlans = response.plans
                queries.transaction {
                    apiPlans.forEach { apiPlan ->
                        queries.upsertPlanEntity(apiPlan.asPlanEntity())
                        stepRepository.saveLocally(apiPlan.steps.toSteps())
                        budgetRepository.saveLocally(apiPlan.budgets.toBudgets())
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
                queries.upsertPlanEntity(planEntity)
                planEntity.asPlan()
            }
        }
    }

    override suspend fun updatePlan(plan: Plan): ApiResponse<Plan> {
        return withContext(ioDispatcher + NonCancellable) {
            planService.updateUserPlan(planId = plan.id.value, request = plan.asRequest())
                .mapOnSuccess { apiPlan ->
                    val planEntity = apiPlan.asPlanEntity()
                    queries.upsertPlanEntity(planEntity)
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

    override fun getPlanByIDAsFlow(id: ID): Flow<Plan> {
        return queries.getPlanEntityByID(id.value)
            .asFlow()
            .mapToOne(ioDispatcher)
            .map { it.asPlan() }
    }
}