package com.closedcircuit.closedcircuitapplication.sponsor.data.fundrequest

import com.closedcircuit.closedcircuitapplication.common.domain.model.Amount
import com.closedcircuit.closedcircuitapplication.common.domain.model.Currency
import com.closedcircuit.closedcircuitapplication.common.domain.model.Date
import com.closedcircuit.closedcircuitapplication.common.domain.model.FundType
import com.closedcircuit.closedcircuitapplication.common.domain.model.ID
import com.closedcircuit.closedcircuitapplication.sponsor.domain.fundrequest.SponsorFundRequest

fun SponsorApiFundRequest.toFundRequest(currency: Currency): SponsorFundRequest {
    return SponsorFundRequest(
        id = ID(id),
        fundType = FundType.fromText(meansOfSupport),
        minimumLoanRange = minimumLoanRange?.toDouble()?.let { Amount(it, currency) },
        maximumLoanRange = maximumLoanRange?.toDouble()?.let { Amount(it, currency) },
        currency = currency,
        maxLenders = maxLenders,
        graceDuration = graceDuration,
        repaymentDuration = repaymentDuration,
        interestRate = interestRate,
        createdAt = Date(createdAt.orEmpty()),
        updatedAt = Date(updatedAt.orEmpty())
    )
}