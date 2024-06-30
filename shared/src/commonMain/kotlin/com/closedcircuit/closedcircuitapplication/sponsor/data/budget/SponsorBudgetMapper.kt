package com.closedcircuit.closedcircuitapplication.sponsor.data.budget

import com.closedcircuit.closedcircuitapplication.common.domain.model.Amount
import com.closedcircuit.closedcircuitapplication.common.domain.model.Date
import com.closedcircuit.closedcircuitapplication.common.domain.model.ID
import com.closedcircuit.closedcircuitapplication.sponsor.data.budget.dto.FundedBudgetDto
import com.closedcircuit.closedcircuitapplication.sponsor.domain.budget.FundedBudget

fun FundedBudgetDto.toFundedBudget() = FundedBudget(
    id = ID(id),
    planId = ID(planID),
    stepId = ID(stepID),
    userId = ID(userID),
    name = name,
    description = description,
    cost = Amount(cost.toDouble()),
    fundsRaised = Amount(fundsRaised.toDouble()),
    isSponsored = isSponsored,
    isFunded = isFunded,
    approverIds = approverIds.map { ID(it) },
    createdAt = Date(createdAt),
    updatedAt = Date(updatedAt)
)

fun List<FundedBudgetDto>.toFundedBudgets() = map { it.toFundedBudget() }