package com.closedcircuit.closedcircuitapplication.beneficiary.data.step

import com.closedcircuit.closedcircuitapplication.beneficiary.data.budget.asBudget
import com.closedcircuit.closedcircuitapplication.beneficiary.data.step.dto.ApiStep
import com.closedcircuit.closedcircuitapplication.beneficiary.data.step.dto.SaveStepPayload
import com.closedcircuit.closedcircuitapplication.common.domain.model.Amount
import com.closedcircuit.closedcircuitapplication.common.domain.model.Currency
import com.closedcircuit.closedcircuitapplication.common.domain.model.Date
import com.closedcircuit.closedcircuitapplication.common.domain.model.ID
import com.closedcircuit.closedcircuitapplication.common.domain.model.TaskDuration
import com.closedcircuit.closedcircuitapplication.common.domain.step.Step
import database.StepEntity
import kotlinx.collections.immutable.toImmutableList

fun StepEntity.asStep(): Step {
    val currency = Currency(currency)
    return Step(
        planID = ID(planID),
        userID = ID(userID),
        id = ID(id),
        name = name,
        description = description,
        duration = TaskDuration(duration),
        targetFunds = Amount(targetFunds, currency),
        totalFundsRaised = Amount(totalFundsRaised, currency),
        currency = currency,
        isSponsored = isSponsored,
        status = status,
        createdAt = Date(createdAt),
        updatedAt = Date(updatedAt)
    )
}

fun ApiStep.asStepEntity() = StepEntity(
    planID = planID,
    userID = userID,
    id = id,
    name = name,
    description = description,
    duration = duration.toLong(),
    targetFunds = targetFunds.toDouble(),
    totalFundsRaised = totalFundsRaised.toDouble(),
    currency = currency,
    isSponsored = isSponsored,
    status = status,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun ApiStep.asStep(): Step {
    val currency = Currency(currency)
    return Step(
        planID = ID(planID),
        userID = ID(userID),
        id = ID(id),
        name = name,
        description = description,
        duration = TaskDuration(duration),
        targetFunds = Amount(targetFunds.toDouble(), currency),
        totalFundsRaised = Amount(totalFundsRaised.toDouble(), currency),
        currency = currency,
        budgets = budgets.map { it.asBudget() },
        isSponsored = isSponsored,
        status = status,
        createdAt = Date(createdAt),
        updatedAt = Date(updatedAt)
    )
}

fun Step.asRequest() = SaveStepPayload(
    name = name,
    description = description,
    duration = duration.value,
    targetFunds = targetFunds.value,
    planID = planID.value,
    userID = userID.value
)

fun Step.asEntity() = StepEntity(
    planID = planID.value,
    userID = userID.value,
    id = id.value,
    name = name,
    description = description,
    duration = duration.value.toLong(),
    targetFunds = targetFunds.value,
    totalFundsRaised = totalFundsRaised.value,
    currency = currency.value,
    isSponsored = isSponsored,
    status = status,
    createdAt = createdAt.value,
    updatedAt = updatedAt.value
)

fun List<ApiStep>.toSteps() = this.map { it.asStepEntity().asStep() }.toImmutableList()

fun List<ApiStep>.asStepEntities() = this.map { it.asStepEntity() }

fun List<StepEntity>.asSteps() = this.map { it.asStep() }.toImmutableList()