package com.closedcircuit.closedcircuitapplication.domain.plan

import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow

typealias Plans = ImmutableList<Plan>

interface PlanRepository {

    val allPlansFlow: Flow<Plans>
}