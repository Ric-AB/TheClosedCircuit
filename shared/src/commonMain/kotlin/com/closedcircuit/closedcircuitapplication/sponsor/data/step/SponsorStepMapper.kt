package com.closedcircuit.closedcircuitapplication.sponsor.data.step

import com.closedcircuit.closedcircuitapplication.common.domain.model.Amount
import com.closedcircuit.closedcircuitapplication.common.domain.model.Date
import com.closedcircuit.closedcircuitapplication.common.domain.model.ID
import com.closedcircuit.closedcircuitapplication.common.domain.model.PositiveInt
import com.closedcircuit.closedcircuitapplication.common.domain.model.StepStatus
import com.closedcircuit.closedcircuitapplication.sponsor.data.budget.toFundedBudgets
import com.closedcircuit.closedcircuitapplication.sponsor.data.step.dto.FundedStepDto
import com.closedcircuit.closedcircuitapplication.sponsor.domain.step.FundedStep

fun FundedStepDto.toFundedStep() = FundedStep(
    id = ID(id),
    planId = ID(planId),
    userId = ID(userId),
    name = name,
    description = description,
    duration = PositiveInt(duration),
    targetFunds = Amount(targetFunds.toDouble()),
    totalFundsRaised = Amount(totalFundsRaised.toDouble()),
    isSponsored = isSponsored,
    status = StepStatus.fromText(status),
    budgets = budgets.toFundedBudgets(),
    approverIds = approverIds.map { ID(it) },
    createdAt = Date(createdAt),
    updatedAt = Date(updatedAt)
)

fun List<FundedStepDto>.toFundedSteps() = map { it.toFundedStep() }