package com.closedcircuit.closedcircuitapplication.sponsor.domain.fundrequest

import com.closedcircuit.closedcircuitapplication.common.domain.model.Amount
import com.closedcircuit.closedcircuitapplication.common.domain.model.Currency
import com.closedcircuit.closedcircuitapplication.common.domain.model.Date
import com.closedcircuit.closedcircuitapplication.common.domain.model.FundType
import com.closedcircuit.closedcircuitapplication.common.domain.model.ID
import kotlinx.serialization.Serializable

@Serializable
data class SponsorFundRequest(
    val id: ID,
    val fundType: FundType,
    val minimumLoanRange: Amount?,
    val maximumLoanRange: Amount?,
    val currency: Currency?,
    val maxLenders: Int?,
    val graceDuration: Int?,
    val repaymentDuration: Int?,
    val interestRate: Int?,
    val createdAt: Date,
    val updatedAt: Date
)
