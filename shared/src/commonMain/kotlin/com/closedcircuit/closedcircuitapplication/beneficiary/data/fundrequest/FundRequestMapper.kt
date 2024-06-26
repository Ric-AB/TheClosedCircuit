package com.closedcircuit.closedcircuitapplication.beneficiary.data.fundrequest

import com.closedcircuit.closedcircuitapplication.common.data.fundrequest.dto.ApiFundRequest
import com.closedcircuit.closedcircuitapplication.common.domain.fundrequest.FundRequest
import com.closedcircuit.closedcircuitapplication.common.domain.model.Date
import com.closedcircuit.closedcircuitapplication.common.domain.model.FundType
import com.closedcircuit.closedcircuitapplication.common.domain.model.ID
import com.closedcircuit.closedcircuitapplication.common.domain.model.Amount

fun FundRequest.toApiFundRequest(): ApiFundRequest {
    return if (fundType == FundType.DONATION) {
        ApiFundRequest(
            planId = planId?.value,
            meansOfSupport = fundType.requestValue,
            minimumLoanRange = null,
            maximumLoanRange = null,
            maxLenders = null,
            graceDuration = null,
            repaymentDuration = null,
            interestRate = null,
            id = null,
            beneficiaryId = null,
            createdAt = null,
            updatedAt = null
        )
    } else {
        ApiFundRequest(
            meansOfSupport = fundType.requestValue,
            minimumLoanRange = minimumLoanRange?.value.toString(),
            maximumLoanRange = maximumLoanRange?.value.toString(),
            maxLenders = maxLenders,
            graceDuration = graceDuration,
            repaymentDuration = repaymentDuration,
            interestRate = interestRate,
            planId = planId?.value,
            id = null,
            beneficiaryId = null,
            createdAt = null,
            updatedAt = null
        )
    }
}

fun ApiFundRequest.toFundRequest(): FundRequest {
    return FundRequest(
        id = ID(id.orEmpty()),
        planId = planId?.let { ID(it) },
        beneficiaryId = beneficiaryId?.let { ID(it) },
        fundType = FundType.fromText(meansOfSupport),
        minimumLoanRange = minimumLoanRange?.toDouble()?.let { Amount(it) },
        maximumLoanRange = maximumLoanRange?.toDouble()?.let { Amount(it) },
        maxLenders = maxLenders,
        graceDuration = graceDuration,
        repaymentDuration = repaymentDuration,
        interestRate = interestRate,
        createdAt = Date(createdAt.orEmpty()),
        updatedAt = Date(updatedAt.orEmpty())
    )
}