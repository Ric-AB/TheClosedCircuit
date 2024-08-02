package com.closedcircuit.closedcircuitapplication.common.domain.plan

import com.closedcircuit.closedcircuitapplication.common.domain.model.ID
import com.closedcircuit.closedcircuitapplication.core.network.ApiResponse
import kotlinx.coroutines.flow.Flow

typealias Plans = List<Plan>

interface PlanRepository {

    fun getPlansAsFlow(): Flow<Plans>

    suspend fun fetchPlans(): ApiResponse<Plans>

    suspend fun createPlan(plan: Plan): ApiResponse<Plan>

    suspend fun updatePlan(plan: Plan): ApiResponse<Plan>

    suspend fun deletePlan(id: ID): ApiResponse<Plan>

    fun getPlanByIDAsFlow(id: ID): Flow<Plan?>

    fun getRecentPlans(limit: Int = 3): Flow<Plans>

    suspend fun fetchPlanByID(id: ID): ApiResponse<Plan>
}