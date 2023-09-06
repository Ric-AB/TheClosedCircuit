package com.closedcircuit.closedcircuitapplication.domain.plan

import kotlinx.coroutines.flow.Flow

interface PlanRepository {

    val allPlansFlow: Flow<List<Plan>>
}