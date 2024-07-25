package com.closedcircuit.closedcircuitapplication.sponsor.domain.loan

import com.closedcircuit.closedcircuitapplication.common.domain.model.Amount
import com.closedcircuit.closedcircuitapplication.common.domain.model.Currency
import com.closedcircuit.closedcircuitapplication.common.domain.model.ImageUrl
import com.closedcircuit.closedcircuitapplication.common.domain.model.Date
import com.closedcircuit.closedcircuitapplication.common.domain.model.ID
import com.closedcircuit.closedcircuitapplication.common.domain.model.LoanStatus
import com.closedcircuit.closedcircuitapplication.common.domain.model.Name
import com.closedcircuit.closedcircuitapplication.common.domain.model.PositiveInt
import com.closedcircuit.closedcircuitapplication.sponsor.presentation.feature.makeoffer.FundingLevel
import kotlinx.serialization.Serializable

@Serializable
data class LoanOfferDetails(
    val id: ID,
    val interestAmount: Amount,
    val loanAmount: Amount,
    val repaymentAmount: Amount?,
    val graceDuration: PositiveInt,
    val sponsorFullName: Name,
    val avatar: ImageUrl,
    val interestRate: PositiveInt,
    val repaymentDuration: PositiveInt,
    val createdAt: Date,
    val updatedAt: Date,
    val fundingLevel: FundingLevel?,
    val otherAmount: Amount?,
    val currency: Currency?,
    val status: LoanStatus?,
    val fundRequestId: ID,
    val sponsorId: ID,
    val stepIds: List<ID>,
    val budgetIds: List<ID>,
    val isStep: Boolean?,
    val isBudget: Boolean?,
    val isOtherAmount: Boolean?,
)
