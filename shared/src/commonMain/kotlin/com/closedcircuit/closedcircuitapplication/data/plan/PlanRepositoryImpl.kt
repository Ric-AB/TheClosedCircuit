package com.closedcircuit.closedcircuitapplication.data.plan

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import com.closedcircuit.closedcircuitapplication.core.network.ApiResponse
import com.closedcircuit.closedcircuitapplication.core.network.ApiSuccessResponse
import com.closedcircuit.closedcircuitapplication.core.network.mapOnSuccess
import com.closedcircuit.closedcircuitapplication.database.TheClosedCircuitDatabase
import com.closedcircuit.closedcircuitapplication.domain.model.ID
import com.closedcircuit.closedcircuitapplication.domain.plan.Plan
import com.closedcircuit.closedcircuitapplication.domain.plan.PlanRepository
import com.closedcircuit.closedcircuitapplication.domain.plan.Plans
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.seconds

class PlanRepositoryImpl(
    database: TheClosedCircuitDatabase,
    private val planService: PlanService,
    private val ioDispatcher: CoroutineDispatcher,
    private val defaultDispatcher: CoroutineDispatcher
) : PlanRepository {
    private val queries = database.planEntityQueries

    override val plansFlow: Flow<Plans> = queries.getPlanEntities()
        .asFlow()
        .mapToList(defaultDispatcher)
        .map { plans -> plans.asPlans() }

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
            val planEntity = plan.asEntity()
            queries.upsertPlanEntity(planEntity)
            delay(3.seconds)
            ApiSuccessResponse(planEntity.asPlan())
//            planService.createPlan(plan.asRequest()).mapOnSuccess { apiPlan ->
//                val planEntity = apiPlan.asPlanEntity()
//                queries.upsertPlanEntity(planEntity)
//                planEntity.asPlan()
//            }
        }
    }

    override suspend fun updatePlan(plan: Plan): ApiResponse<Plan> {
        return withContext(ioDispatcher + NonCancellable) {
            val planEntity = plan.asEntity()
            queries.upsertPlanEntity(planEntity)
            delay(3.seconds)
            ApiSuccessResponse(planEntity.asPlan())
//            planService.updateUserPlan(planId = plan.id.value, request = plan.asRequest())
//                .mapOnSuccess { apiPlan ->
//                    val planEntity = apiPlan.asPlanEntity()
//                    queries.upsertPlanEntity(planEntity)
//                    planEntity.asPlan()
//                }
        }
    }

    override suspend fun deletePlan(id: ID): ApiResponse<Plan> {
        val idValue = id.value
        return withContext(ioDispatcher + NonCancellable) {
            val planEntity = queries.getPlanEntityByID(idValue).executeAsOne()
            queries.deletePlanEntity(idValue)
            ApiSuccessResponse(planEntity.asPlan())
//            planService.deletePlan(idValue).mapOnSuccess {
//                val planEntity = queries.getPlanEntityByID(idValue).executeAsOne()
//                queries.deletePlanEntity(idValue)
//                planEntity.asPlan()
//            }
        }
    }

    override fun getPlanByIDAsFlow(id: ID): Flow<Plan> {
        return queries.getPlanEntityByID(id.value)
            .asFlow()
            .mapToOne(ioDispatcher)
            .map { it.asPlan() }
    }
}