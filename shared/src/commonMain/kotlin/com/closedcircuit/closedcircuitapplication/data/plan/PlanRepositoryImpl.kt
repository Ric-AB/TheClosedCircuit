package com.closedcircuit.closedcircuitapplication.data.plan

import com.closedcircuit.closedcircuitapplication.domain.model.Avatar
import com.closedcircuit.closedcircuitapplication.domain.model.ID
import com.closedcircuit.closedcircuitapplication.domain.model.Price
import com.closedcircuit.closedcircuitapplication.domain.model.TaskDuration
import com.closedcircuit.closedcircuitapplication.domain.plan.Plan
import com.closedcircuit.closedcircuitapplication.domain.plan.PlanRepository
import com.closedcircuit.closedcircuitapplication.domain.plan.Plans
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class PlanRepositoryImpl : PlanRepository {
    private val _allPlans = MutableStateFlow(plans)
    override val allPlansFlow: Flow<Plans>
        get() = _allPlans


}

private val plans = (1..10).map {
    Plan(
        id = ID("0d410f4e-4cd6-11ee-be56-0242ac120002"),
        avatar = Avatar(""),
        planCategory = "Business",
        planSector = "Tech",
        planType = "Plan type",
        businessName = "New name",
        planDescription = "",
        planDuration = TaskDuration(3),
        estimatedSellingPrice = Price("10000"),
        estimatedCostPrice = Price("5000"),
        fundsRaised = Price("2000"),
        tasksCompleted = "",
        targetAmount = Price("3000"),
        totalFundsRaised = Price("2000"),
        planAnalytics = "Completed",
        user = "",
        hasRequestedFund = true,
        isSponsored = true,
        accountabilityPartners = emptyList()
    )
}.toImmutableList()