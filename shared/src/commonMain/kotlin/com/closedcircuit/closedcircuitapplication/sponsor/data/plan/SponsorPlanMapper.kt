package com.closedcircuit.closedcircuitapplication.sponsor.data.plan

import com.closedcircuit.closedcircuitapplication.beneficiary.data.budget.asBudgetEntities
import com.closedcircuit.closedcircuitapplication.beneficiary.data.budget.asBudgets
import com.closedcircuit.closedcircuitapplication.beneficiary.data.fundrequest.toFundRequest
import com.closedcircuit.closedcircuitapplication.beneficiary.data.step.asStepEntities
import com.closedcircuit.closedcircuitapplication.beneficiary.data.step.asSteps
import com.closedcircuit.closedcircuitapplication.common.domain.model.Amount
import com.closedcircuit.closedcircuitapplication.common.domain.model.Avatar
import com.closedcircuit.closedcircuitapplication.common.domain.model.Currency
import com.closedcircuit.closedcircuitapplication.common.domain.model.Date
import com.closedcircuit.closedcircuitapplication.common.domain.model.FundType
import com.closedcircuit.closedcircuitapplication.common.domain.model.ID
import com.closedcircuit.closedcircuitapplication.common.domain.model.Name
import com.closedcircuit.closedcircuitapplication.common.domain.model.PositiveInt
import com.closedcircuit.closedcircuitapplication.common.domain.model.TaskDuration
import com.closedcircuit.closedcircuitapplication.common.util.orFalse
import com.closedcircuit.closedcircuitapplication.common.util.orZero
import com.closedcircuit.closedcircuitapplication.sponsor.data.plan.dto.ApiPlan
import com.closedcircuit.closedcircuitapplication.sponsor.data.plan.dto.FundedPlanDto
import com.closedcircuit.closedcircuitapplication.sponsor.data.plan.dto.FundedPlanPreviewDto
import com.closedcircuit.closedcircuitapplication.sponsor.domain.plan.FundedPlan
import com.closedcircuit.closedcircuitapplication.sponsor.domain.plan.FundedPlanPreview
import com.closedcircuit.closedcircuitapplication.sponsor.domain.plan.SponsorPlan
import com.closedcircuit.closedcircuitapplication.sponsor.presentation.feature.makeoffer.FundingLevel

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

fun FundedPlanPreviewDto.toDashboardPlan() = FundedPlanPreview(
    id = ID(id),
    avatar = Avatar(avatar),
    sector = sector,
    beneficiaryFullName = Name(beneficiaryFullName),
    beneficiaryId = beneficiaryId?.let { ID(it) },
    currency = currency?.let { Currency(it) },
    amountFunded = Amount(amountFunded.toDouble()),
    fundingType = FundType.fromText(fundingType),
    fundingLevel = FundingLevel.fromText(fundingLevel),
    fundingDate = Date(fundingDate),
    fundsRaisedPercent = fundsRaisedPercent,
    tasksCompletedPercent = tasksCompletedPercent
)

fun FundedPlanDto.toFundedPlan() = FundedPlan(
    id = ID(id),
    avatar = Avatar(avatar),
    name = name,
    description = description,
    category = category,
    sector = sector,
    type = type,
    duration = PositiveInt(duration),
    estimatedSellingPrice = Amount(estimatedSellingPrice),
    estimatedCostPrice = Amount(estimatedCostPrice),
    targetAmount = Amount(targetAmount),
    analytics = analytics,
    userID = ID(userId),
    beneficiaryId =  ID(beneficiaryId),
    fundRequestID = ID(fundRequestId),
    currency = Currency(currency),
    hasRequestedFunds = hasRequestedFund,
    isSponsored = isSponsored,
    accountabilityPartnerIds = accountabilityPartnerIds.map { ID(it) },
    createdAt = Date(createdAt),
    updatedAt = Date(updatedAt)
)

fun List<FundedPlanPreviewDto>.toDashboardPlans() = map { it.toDashboardPlan() }