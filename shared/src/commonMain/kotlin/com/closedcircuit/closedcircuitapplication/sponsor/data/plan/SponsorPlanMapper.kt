package com.closedcircuit.closedcircuitapplication.sponsor.data.plan

import com.closedcircuit.closedcircuitapplication.beneficiary.data.budget.asBudgetEntities
import com.closedcircuit.closedcircuitapplication.beneficiary.data.budget.asBudgets
import com.closedcircuit.closedcircuitapplication.beneficiary.data.fundrequest.toFundRequest
import com.closedcircuit.closedcircuitapplication.beneficiary.data.step.asStepEntities
import com.closedcircuit.closedcircuitapplication.beneficiary.data.step.asSteps
import com.closedcircuit.closedcircuitapplication.common.domain.model.Amount
import com.closedcircuit.closedcircuitapplication.common.domain.model.Avatar
import com.closedcircuit.closedcircuitapplication.common.domain.model.ID
import com.closedcircuit.closedcircuitapplication.common.domain.model.Name
import com.closedcircuit.closedcircuitapplication.common.domain.model.TaskDuration
import com.closedcircuit.closedcircuitapplication.common.util.orFalse
import com.closedcircuit.closedcircuitapplication.common.util.orZero
import com.closedcircuit.closedcircuitapplication.sponsor.data.plan.dto.ApiPlan
import com.closedcircuit.closedcircuitapplication.sponsor.domain.plan.SponsorPlan

fun ApiPlan.asSponsorPlan() = SponsorPlan(
    id = ID(id),
    avatar = Avatar(avatar),
    category = category,
    sector = sector,
    type = type,
    name = name,
    description = description,
    duration = TaskDuration(duration),
    estimatedSellingPrice = Amount(estimatedSellingPrice.toDouble()),
    estimatedCostPrice = Amount(estimatedCostPrice.toDouble()),
    fundsRaised = fundsRaised.orZero(),
    tasksCompleted = tasksCompleted.orZero(),
    targetAmount = Amount(targetAmount.toDouble()),
    totalFundsRaised = Amount(totalFundsRaised?.toDouble().orZero()),
    analytics = analytics,
    userID = ID(user),
    hasRequestedFund = hasRequestedFund.orFalse(),
    isSponsored = isSponsored.orFalse(),
    fundRequest = fundRequest.toFundRequest(),
    beneficiaryFullName = Name(beneficiaryFullName.orEmpty()),
    beneficiaryId = beneficiaryId?.let { ID(it) },
    steps = steps.asStepEntities().asSteps(),
    budgets = budgets.asBudgetEntities().asBudgets()
)