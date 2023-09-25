package com.closedcircuit.closedcircuitapplication.domain.plan

import com.closedcircuit.closedcircuitapplication.core.network.ApiResponse
import com.closedcircuit.closedcircuitapplication.domain.model.ID
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow

typealias Plans = ImmutableList<Plan>

interface PlanRepository {

    val plansFlow: Flow<Plans>

    suspend fun fetchPlans(): ApiResponse<Plans>

    suspend fun createPlan(plan: Plan): ApiResponse<Plan>

    suspend fun updatePlan(plan: Plan): ApiResponse<Plan>

    suspend fun deletePlan(id: ID): ApiResponse<Plan>
}