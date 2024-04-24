package com.closedcircuit.closedcircuitapplication.beneficiary.data.budget

import com.closedcircuit.closedcircuitapplication.beneficiary.data.budget.dto.ApiBudget
import com.closedcircuit.closedcircuitapplication.beneficiary.data.budget.dto.SaveBudgetPayload
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.budget.Budget
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.model.Date
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.model.ID
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.model.Amount
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
    cost = Amount(cost),
    isSponsored = isSponsored,
    fundsRaised = Amount(fundsRaised),
    isCompleted = isCompleted,
    approvers = emptyList(),
    createdAt = Date(createdAt),
    updatedAt = Date(updatedAt)
)

fun Budget.asRequest() = SaveBudgetPayload(
    name = name,
    description = description,
    cost = cost.value,
    planID = planID.value,
    stepID = stepID.value
)

fun Budget.asEntity() = BudgetEntity(
    id = id.value,
    planID = planID.value,
    stepID = stepID.value,
    userID = userID.value,
    name = name,
    description = description,
    cost = cost.value,
    isSponsored = isSponsored,
    isCompleted = isCompleted,
    fundsRaised = fundsRaised.value,
    createdAt = createdAt.value,
    updatedAt = updatedAt.value
)

fun List<ApiBudget>.toBudgets() = this.map { it.asBudgetEntity().asBudget() }

fun List<ApiBudget>.asBudgetEntities() = this.map { it.asBudgetEntity() }

fun List<BudgetEntity>.asBudgets() = this.map { it.asBudget() }.toImmutableList()