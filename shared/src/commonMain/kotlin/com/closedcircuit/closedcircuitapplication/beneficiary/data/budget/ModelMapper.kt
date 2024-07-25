package com.closedcircuit.closedcircuitapplication.beneficiary.data.budget

import com.closedcircuit.closedcircuitapplication.beneficiary.data.budget.dto.ApiBudget
import com.closedcircuit.closedcircuitapplication.beneficiary.data.budget.dto.SaveBudgetPayload
import com.closedcircuit.closedcircuitapplication.common.data.model.toFile
import com.closedcircuit.closedcircuitapplication.common.domain.budget.Budget
import com.closedcircuit.closedcircuitapplication.common.domain.model.Amount
import com.closedcircuit.closedcircuitapplication.common.domain.model.Currency
import com.closedcircuit.closedcircuitapplication.common.domain.model.Date
import com.closedcircuit.closedcircuitapplication.common.domain.model.ID
import com.closedcircuit.closedcircuitapplication.common.util.orFalse
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
    currency = currency,
    isSponsored = isSponsored,
    isCompleted = isCompleted.orFalse(),
    fundsRaised = fundsRaised.toDouble(),
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun ApiBudget.asBudget(): Budget {
    val currency = Currency(currency)
    return Budget(
        id = ID(id),
        planID = ID(planID),
        stepID = ID(stepID),
        userID = ID(userID),
        name = name,
        description = description,
        cost = Amount(cost.toDouble(), currency),
        isSponsored = isSponsored,
        fundsRaised = Amount(fundsRaised.toDouble(), currency),
        currency = currency,
        isCompleted = isCompleted.orFalse(),
        proofs = proof.orEmpty().map { it.toFile() },
        approvers = approvers.map { ID(it) },
        createdAt = Date(createdAt),
        updatedAt = Date(updatedAt)
    )
}

fun BudgetEntity.asBudget(): Budget {
    val currency = Currency(currency)
    return Budget(
        id = ID(id),
        planID = ID(planID),
        stepID = ID(stepID),
        userID = ID(userID),
        name = name,
        description = description,
        cost = Amount(cost),
        isSponsored = isSponsored,
        fundsRaised = Amount(fundsRaised, currency),
        currency = currency,
        isCompleted = isCompleted,
        proofs = emptyList(),
        approvers = emptyList(),
        createdAt = Date(createdAt),
        updatedAt = Date(updatedAt)
    )
}

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
    currency = currency.value,
    isSponsored = isSponsored,
    isCompleted = isCompleted,
    fundsRaised = fundsRaised.value,
    createdAt = createdAt.value,
    updatedAt = updatedAt.value
)

fun List<ApiBudget>.toBudgets() = this.map { it.asBudgetEntity().asBudget() }

fun List<ApiBudget>.asBudgetEntities() = this.map { it.asBudgetEntity() }

fun List<BudgetEntity>.asBudgets() = this.map { it.asBudget() }.toImmutableList()