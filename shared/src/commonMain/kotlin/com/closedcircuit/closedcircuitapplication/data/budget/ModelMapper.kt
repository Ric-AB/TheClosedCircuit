package com.closedcircuit.closedcircuitapplication.data.budget

import com.closedcircuit.closedcircuitapplication.data.budget.dto.ApiBudget
import com.closedcircuit.closedcircuitapplication.data.budget.dto.CreateOrUpdateBudgetRequest
import com.closedcircuit.closedcircuitapplication.domain.budget.Budget
import com.closedcircuit.closedcircuitapplication.domain.model.Date
import com.closedcircuit.closedcircuitapplication.domain.model.ID
import com.closedcircuit.closedcircuitapplication.domain.model.Price
import database.BudgetEntity
import kotlinx.collections.immutable.toImmutableList

fun ApiBudget.asBudgetEntity() = BudgetEntity(
    id = id,
    planID = planID,
    stepID = stepID,
    userID = userID,
    name = name,
    description = description,
    cost = cost.toDouble(),
    isSponsored = isSponsored,
    isCompleted = isCompleted ?: false,
    fundsRaised = fundsRaised.toDouble(),
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun BudgetEntity.asBudget() = Budget(
    id = ID(id),
    planID = ID(planID),
    stepID = ID(stepID),
    userID = ID(userID),
    name = name,
    description = description,
    cost = Price(cost),
    isSponsored = isSponsored,
    fundsRaised = Price(fundsRaised),
    isCompleted = isCompleted,
    approvers = emptyList(),
    createdAt = Date(createdAt),
    updatedAt = Date(updatedAt)
)

fun Budget.asRequest() = CreateOrUpdateBudgetRequest(
    name = name,
    description = description,
    cost = cost.value,
    planID = planID.value,
    stepID = stepID.value
)

fun List<ApiBudget>.asBudgetEntities() = this.map { it.asBudgetEntity() }

fun List<BudgetEntity>.asBudgets() = this.map { it.asBudget() }.toImmutableList()