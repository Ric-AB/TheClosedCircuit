package com.closedcircuit.closedcircuitapplication.sponsor.data.plan

import com.closedcircuit.closedcircuitapplication.beneficiary.data.fundrequest.toFundRequest
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.model.Amount
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.model.Avatar
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.model.ID
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.model.Name
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.model.TaskDuration
import com.closedcircuit.closedcircuitapplication.common.data.plan.dto.ApiPlan
import com.closedcircuit.closedcircuitapplication.common.util.orFalse
import com.closedcircuit.closedcircuitapplication.common.util.orZero
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
    beneficiaryId = beneficiaryId?.let { ID(it) }
)