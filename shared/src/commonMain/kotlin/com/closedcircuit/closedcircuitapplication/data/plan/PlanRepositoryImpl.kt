package com.closedcircuit.closedcircuitapplication.data.plan

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.closedcircuit.closedcircuitapplication.core.network.ApiResponse
import com.closedcircuit.closedcircuitapplication.core.network.mapOnSuccess
import com.closedcircuit.closedcircuitapplication.database.TheClosedCircuitDatabase
import com.closedcircuit.closedcircuitapplication.domain.model.ID
import com.closedcircuit.closedcircuitapplication.domain.plan.Plan
import com.closedcircuit.closedcircuitapplication.domain.plan.PlanRepository
import com.closedcircuit.closedcircuitapplication.domain.plan.Plans
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class PlanRepositoryImpl(
    database: TheClosedCircuitDatabase,
    private val planService: PlanService,
    private val ioDispatcher: CoroutineDispatcher,
    defaultDispatcher: CoroutineDispatcher
) : PlanRepository {
    private val queries = database.planEntityQueries

    override val plansFlow: Flow<Plans> = queries.getPlanEntities()
        .asFlow()
        .mapToList(defaultDispatcher)
        .map { plans ->
            plans.asPlans()
        }

    override suspend fun fetchPlans(): ApiResponse<Plans> {
        return withContext(ioDispatcher + NonCancellable) {
            planService.getPlans().mapOnSuccess { response ->
                val planEntities = response.plans.asPlanEntities()
                queries.transaction {
                    planEntities.forEach { planEntity ->
                        queries.upsertPlanEntity(planEntity)
                    }
                }

                planEntities.asPlans()
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
                queries.deletPlanEntity(idValue)
                planEntity.asPlan()
            }
        }
    }
}