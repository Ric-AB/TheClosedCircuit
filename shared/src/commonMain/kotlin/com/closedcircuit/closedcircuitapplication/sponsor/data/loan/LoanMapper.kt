package com.closedcircuit.closedcircuitapplication.sponsor.data.loan

import com.closedcircuit.closedcircuitapplication.common.domain.model.Amount
import com.closedcircuit.closedcircuitapplication.common.domain.model.ImageUrl
import com.closedcircuit.closedcircuitapplication.common.domain.model.Date
import com.closedcircuit.closedcircuitapplication.common.domain.model.ID
import com.closedcircuit.closedcircuitapplication.common.domain.model.LoanStatus
import com.closedcircuit.closedcircuitapplication.common.domain.model.Name
import com.closedcircuit.closedcircuitapplication.common.domain.model.PositiveInt
import com.closedcircuit.closedcircuitapplication.sponsor.data.loan.dto.LoanOfferDetailsDto
import com.closedcircuit.closedcircuitapplication.sponsor.data.loan.dto.LoanOfferDto
import com.closedcircuit.closedcircuitapplication.sponsor.domain.loan.LoanOffer
import com.closedcircuit.closedcircuitapplication.sponsor.domain.loan.LoanOfferDetails
import com.closedcircuit.closedcircuitapplication.sponsor.presentation.feature.makeoffer.FundingLevel

fun LoanOfferDto.toLoanOffer() = LoanOffer(
    loanOfferId = loanOfferId?.let { ID(it) },
    businessName = businessName,
    beneficiaryName = beneficiaryName?.let { Name(it) },
    beneficiaryAvatar = beneficiaryAvatar?.let { ImageUrl(it) },
    planSector = planSector,
    loanAmount = loanAmount?.let { Amount(it.toDouble()) },
    createdAt = createdAt?.let { Date(it) }
)

fun LoanOfferDetailsDto.toLoanOffer() = LoanOfferDetails(
    id = ID(id),
    interestAmount = Amount(interestAmount.toDouble()),
    loanAmount = Amount(loanAmount.toDouble()),
    repaymentAmount = Amount(repaymentAmount.toDouble()),
    graceDuration = PositiveInt(gracePeriod),
    sponsorFullName = Name(sponsorFullName),
    avatar = ImageUrl(avatar),
    interestRate = PositiveInt(interestRate),
    repaymentDuration = PositiveInt(repaymentDuration),
    createdAt = Date(createdAt),
    updatedAt = Date(updatedAt),
    fundingLevel = FundingLevel.fromText(fundingLevel),
    otherAmount = otherAmount?.let { Amount(it.toDouble()) },
    status = LoanStatus.fromText(status),
    fundRequestId = ID(fundRequest),
    sponsorId = ID(sponsor),
    stepIds = steps.map { ID(it) },
    budgetIds = budgets.map { ID(it) },
    isStep = isStep,
    isBudget = isBudget,
    isOtherAmount = isOtherAmount
)

fun List<LoanOfferDto>.toLoanOffers() = map { it.toLoanOffer() }