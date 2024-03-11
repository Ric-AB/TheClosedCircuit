package com.closedcircuit.closedcircuitapplication.data.step

import com.closedcircuit.closedcircuitapplication.data.step.dto.ApiStep
import com.closedcircuit.closedcircuitapplication.data.step.dto.CreateOrUpdateStepRequest
import com.closedcircuit.closedcircuitapplication.domain.model.Date
import com.closedcircuit.closedcircuitapplication.domain.model.ID
import com.closedcircuit.closedcircuitapplication.domain.model.Amount
import com.closedcircuit.closedcircuitapplication.domain.model.TaskDuration
import com.closedcircuit.closedcircuitapplication.domain.step.Step
import database.StepEntity
import kotlinx.collections.immutable.toImmutableList

fun StepEntity.asStep() = Step(
    id = ID(id),
    name = name,
    description = description,
    duration = TaskDuration(duration),
    targetFunds = Amount(targetFunds),
    totalFundsRaised = Amount(totalFundsRaised),
    planID = ID(planID),
    userID = ID(userID),
    isSponsored = isSponsored,
    status = status,
    createdAt = Date(createdAt),
    updatedAt = Date(updatedAt)
)

fun ApiStep.asStepEntity() = StepEntity(
    id = id,
    name = name,
    description = description,
    duration = duration.toLong(),
    targetFunds = targetFunds.toDouble(),
    totalFundsRaised = totalFundsRaised.toDouble(),
    planID = planID,
    userID = userID,
    isSponsored = isSponsored,
    status = status,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun Step.asRequest() = CreateOrUpdateStepRequest(
    name = name,
    description = description,
    duration = duration.value,
    targetFunds = targetFunds.value,
    planID = planID.value,
    userID = userID.value
)

fun Step.asEntity() = StepEntity(
    id = id.value,
    name = name,
    description = description,
    duration = duration.value.toLong(),
    targetFunds = targetFunds.value,
    totalFundsRaised = totalFundsRaised.value,
    planID = planID.value,
    userID = userID.value,
    isSponsored = isSponsored,
    status = status,
    createdAt = createdAt.value,
    updatedAt = updatedAt.value
)

fun List<ApiStep>.toSteps() = this.map { it.asStepEntity().asStep() }.toImmutableList()

fun List<ApiStep>.asStepEntities() = this.map { it.asStepEntity() }

fun List<StepEntity>.asSteps() = this.map { it.asStep() }.toImmutableList()