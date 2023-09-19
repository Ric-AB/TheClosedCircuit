package com.closedcircuit.closedcircuitapplication.data.step

import com.closedcircuit.closedcircuitapplication.data.step.dto.ApiStep
import com.closedcircuit.closedcircuitapplication.domain.model.Date
import com.closedcircuit.closedcircuitapplication.domain.model.ID
import com.closedcircuit.closedcircuitapplication.domain.model.Price
import com.closedcircuit.closedcircuitapplication.domain.model.TaskDuration
import com.closedcircuit.closedcircuitapplication.domain.step.Step
import database.StepEntity
import kotlinx.collections.immutable.toImmutableList

fun StepEntity.asStep() = Step(
    id = ID(id),
    name = name,
    description = description,
    duration = TaskDuration(duration),
    targetFunds = Price(targetFunds),
    totalFundsRaised = Price(totalFundsRaised),
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

fun List<ApiStep>.asStepEntities() = this.map { it.asStepEntity() }

fun List<StepEntity>.asSteps() = this.map { it.asStep() }.toImmutableList()