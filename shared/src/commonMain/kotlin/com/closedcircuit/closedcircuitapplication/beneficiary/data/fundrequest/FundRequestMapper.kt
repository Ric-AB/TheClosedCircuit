package com.closedcircuit.closedcircuitapplication.beneficiary.data.fundrequest

import com.closedcircuit.closedcircuitapplication.common.data.fundrequest.dto.ApiFundRequest
import com.closedcircuit.closedcircuitapplication.common.domain.fundrequest.FundRequest
import com.closedcircuit.closedcircuitapplication.common.domain.model.Amount
import com.closedcircuit.closedcircuitapplication.common.domain.model.Currency
import com.closedcircuit.closedcircuitapplication.common.domain.model.Date
import com.closedcircuit.closedcircuitapplication.common.domain.model.FundType
import com.closedcircuit.closedcircuitapplication.common.domain.model.ID
import database.FundRequestEntity
import kotlin.jvm.JvmName

fun FundRequest.toApiFundRequest(): ApiFundRequest {
    return if (fundType == FundType.DONATION) {
        ApiFundRequest(
            planId = planId.value,
            meansOfSupport = fundType.requestValue,
            currency = null,
            minimumLoanRange = null,
            maximumLoanRange = null,
            maxLenders = null,
            graceDuration = null,
            repaymentDuration = null,
            interestRate = null,
            id = id.value,
            beneficiaryId = beneficiaryId.value,
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
            planId = planId.value,
            id = id.value,
            beneficiaryId = beneficiaryId.value,
            currency = null,
            createdAt = null,
            updatedAt = null
        )
    }
}

fun ApiFundRequest.toFundRequest(): FundRequest {
    val currency = currency?.let { Currency(it) }
    return FundRequest(
        id = ID(id),
        planId = ID(planId),
        beneficiaryId = ID(beneficiaryId),
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

fun FundRequest.toFundRequestEntity(): FundRequestEntity {
    return FundRequestEntity(
        meansOfSupport = fundType.requestValue,
        minimumLoanRange = minimumLoanRange?.value,
        maximumLoanRange = maximumLoanRange?.value,
        maxLenders = maxLenders?.toLong(),
        graceDuration = graceDuration?.toLong(),
        repaymentDuration = repaymentDuration?.toLong(),
        interestRate = interestRate?.toLong(),
        planId = planId.value,
        currency = null,
        id = id.value,
        beneficiaryId = beneficiaryId.value,
        createdAt = createdAt.value,
        updatedAt = updatedAt.value
    )
}

fun FundRequestEntity.toFundRequest(): FundRequest {
    val currency = currency?.let { Currency(it) }
    return FundRequest(
        id = ID(id),
        planId = ID(planId),
        beneficiaryId = ID(beneficiaryId),
        fundType = FundType.fromText(meansOfSupport),
        minimumLoanRange = minimumLoanRange?.let { Amount(it, currency) },
        maximumLoanRange = maximumLoanRange?.let { Amount(it, currency) },
        currency = currency,
        maxLenders = maxLenders?.toInt(),
        graceDuration = graceDuration?.toInt(),
        repaymentDuration = repaymentDuration?.toInt(),
        interestRate = interestRate?.toInt(),
        createdAt = Date(createdAt),
        updatedAt = Date(updatedAt)
    )
}

@JvmName("apiFundRequestToDomain")
fun List<ApiFundRequest>.toFundRequests() = map { it.toFundRequest() }

fun List<FundRequestEntity>.toFundRequests() = map { it.toFundRequest() }