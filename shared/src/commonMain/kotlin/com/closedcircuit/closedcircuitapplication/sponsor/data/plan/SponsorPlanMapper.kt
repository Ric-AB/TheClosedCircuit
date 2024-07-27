package com.closedcircuit.closedcircuitapplication.sponsor.data.plan

import com.closedcircuit.closedcircuitapplication.beneficiary.data.budget.toBudgetEntities
import com.closedcircuit.closedcircuitapplication.beneficiary.data.budget.toBudgets
import com.closedcircuit.closedcircuitapplication.beneficiary.data.fundrequest.toFundRequest
import com.closedcircuit.closedcircuitapplication.beneficiary.data.step.toStepEntities
import com.closedcircuit.closedcircuitapplication.beneficiary.data.step.toSteps
import com.closedcircuit.closedcircuitapplication.common.domain.model.Amount
import com.closedcircuit.closedcircuitapplication.common.domain.model.Currency
import com.closedcircuit.closedcircuitapplication.common.domain.model.Date
import com.closedcircuit.closedcircuitapplication.common.domain.model.FundType
import com.closedcircuit.closedcircuitapplication.common.domain.model.ID
import com.closedcircuit.closedcircuitapplication.common.domain.model.ImageUrl
import com.closedcircuit.closedcircuitapplication.common.domain.model.Name
import com.closedcircuit.closedcircuitapplication.common.domain.model.PositiveInt
import com.closedcircuit.closedcircuitapplication.common.domain.model.TaskDuration
import com.closedcircuit.closedcircuitapplication.common.util.orFalse
import com.closedcircuit.closedcircuitapplication.common.util.orZero
import com.closedcircuit.closedcircuitapplication.sponsor.data.plan.dto.ApiPlan
import com.closedcircuit.closedcircuitapplication.sponsor.data.plan.dto.FundedPlanDto
import com.closedcircuit.closedcircuitapplication.sponsor.data.plan.dto.FundedPlanPreviewDto
import com.closedcircuit.closedcircuitapplication.sponsor.data.step.toFundedSteps
import com.closedcircuit.closedcircuitapplication.sponsor.domain.plan.FundedPlan
import com.closedcircuit.closedcircuitapplication.sponsor.domain.plan.FundedPlanPreview
import com.closedcircuit.closedcircuitapplication.sponsor.domain.plan.SponsorPlan
import com.closedcircuit.closedcircuitapplication.sponsor.presentation.feature.makeoffer.FundingLevel

fun ApiPlan.asSponsorPlan(): SponsorPlan {
    val currency = Currency(currency)
    return SponsorPlan(
        id = ID(id),
        avatar = ImageUrl(avatar),
        category = category,
        sector = sector,
        type = type,
        name = name,
        description = description,
        duration = TaskDuration(duration),
        currency = currency,
        estimatedSellingPrice = Amount(estimatedSellingPrice.toDouble(), currency),
        estimatedCostPrice = Amount(estimatedCostPrice.toDouble(), currency),
        fundsRaised = fundsRaised.orZero(),
        tasksCompleted = tasksCompleted.orZero(),
        targetAmount = Amount(targetAmount.toDouble(), currency),
        totalFundsRaised = Amount(totalFundsRaised?.toDouble().orZero(), currency),
        analytics = analytics,
        userID = ID(user),
        hasRequestedFund = hasRequestedFund.orFalse(),
        isSponsored = isSponsored.orFalse(),
        fundRequest = fundRequest.toFundRequest(),
        beneficiaryFullName = Name(beneficiaryFullName.orEmpty()),
        beneficiaryId = beneficiaryId?.let { ID(it) },
        steps = steps.toStepEntities().toSteps(),
        budgets = budgets.toBudgetEntities().toBudgets()
    )
}

fun FundedPlanPreviewDto.toDashboardPlan(): FundedPlanPreview {
    val currency = currency?.let { Currency(it) }
    return FundedPlanPreview(
        id = ID(id),
        avatar = ImageUrl(avatar),
        sector = sector,
        beneficiaryFullName = Name(beneficiaryFullName),
        beneficiaryId = beneficiaryId?.let { ID(it) },
        currency = currency,
        amountFunded = Amount(amountFunded.toDouble(), currency),
        fundingType = FundType.fromText(fundingType),
        fundingLevel = FundingLevel.fromText(fundingLevel),
        fundingDate = Date(fundingDate),
        fundsRaisedPercent = fundsRaisedPercent,
        tasksCompletedPercent = tasksCompletedPercent
    )
}

fun FundedPlanDto.toFundedPlan(): FundedPlan {
    val currency = Currency(currency)
    return FundedPlan(
        id = ID(id),
        avatar = ImageUrl(avatar),
        name = name,
        description = description,
        category = category,
        sector = sector,
        type = type,
        duration = PositiveInt(duration),
        estimatedSellingPrice = Amount(estimatedSellingPrice, currency),
        estimatedCostPrice = Amount(estimatedCostPrice, currency),
        targetAmount = Amount(targetAmount, currency),
        analytics = analytics,
        userID = ID(userId),
        beneficiaryId = ID(beneficiaryId),
        fundRequestID = ID(fundRequestId),
        currency = currency,
        hasRequestedFunds = hasRequestedFund,
        isSponsored = isSponsored,
        steps = steps.toFundedSteps(),
        accountabilityPartnerIds = accountabilityPartnerIds.map { ID(it) },
        createdAt = Date(createdAt),
        updatedAt = Date(updatedAt)
    )
}

fun List<FundedPlanPreviewDto>.toDashboardPlans() = map { it.toDashboardPlan() }