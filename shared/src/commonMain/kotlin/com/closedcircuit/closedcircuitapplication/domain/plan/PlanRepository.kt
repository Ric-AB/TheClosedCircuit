package com.closedcircuit.closedcircuitapplication.domain.plan

import com.closedcircuit.closedcircuitapplication.core.network.ApiResponse
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow

typealias Plans = ImmutableList<Plan>

interface PlanRepository {

    val plansFlow: Flow<Plans>

    suspend fun fetchPlans(): ApiResponse<Plans>
}