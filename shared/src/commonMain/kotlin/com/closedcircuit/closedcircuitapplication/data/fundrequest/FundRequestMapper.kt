package com.closedcircuit.closedcircuitapplication.data.fundrequest

import com.closedcircuit.closedcircuitapplication.data.fundrequest.dto.ApiFundRequest
import com.closedcircuit.closedcircuitapplication.data.fundrequest.dto.FundRequestPayload
import com.closedcircuit.closedcircuitapplication.domain.fundrequest.FundRequest
import com.closedcircuit.closedcircuitapplication.domain.model.Date
import com.closedcircuit.closedcircuitapplication.domain.model.ID
import com.closedcircuit.closedcircuitapplication.domain.model.Price

fun FundRequest.asRequest(): FundRequestPayload {
    return FundRequestPayload(
        meansOfSupport = meansOfSupport,
        minimumLoanRange = minimumLoanRange?.value.toString(),
        maximumLoanRange = maximumLoanRange?.value.toString(),
        maxLenders = maxLenders.toString(),
        graceDuration = graceDuration.toString(),
        repaymentDuration = repaymentDuration.toString(),
        interestRate = interestRate.toString(),
        planId = planId.value
    )
}

fun FundRequest.toApiFundRequest(): ApiFundRequest {
    return ApiFundRequest(
        meansOfSupport = meansOfSupport,
        minimumLoanRange = minimumLoanRange?.value.toString(),
        maximumLoanRange = maximumLoanRange?.value.toString(),
        maxLenders = maxLenders.toString(),
        graceDuration = graceDuration.toString(),
        repaymentDuration = repaymentDuration.toString(),
        interestRate = interestRate.toString(),
        planId = planId.value,
        id = null,
        beneficiaryId = null,
        createdAt = null,
        updatedAt = null
    )
}

fun ApiFundRequest.toFundRequest(): FundRequest {
    return FundRequest(
        id = ID(id.orEmpty()),
        planId = ID(planId.orEmpty()),
        beneficiaryId = ID(beneficiaryId.orEmpty()),
        meansOfSupport = meansOfSupport.orEmpty(),
        minimumLoanRange = minimumLoanRange?.toDouble()?.let { Price(it) },
        maximumLoanRange = maximumLoanRange?.toDouble()?.let { Price(it) },
        maxLenders = maxLenders?.toInt(),
        graceDuration = graceDuration?.toInt(),
        repaymentDuration = repaymentDuration?.toInt(),
        interestRate = interestRate?.toInt(),
        createdAt = Date(createdAt.orEmpty()),
        updatedAt = Date(updatedAt.orEmpty())
    )
}