package com.closedcircuit.closedcircuitapplication.domain.fundrequest

import com.closedcircuit.closedcircuitapplication.domain.model.Date
import com.closedcircuit.closedcircuitapplication.domain.model.ID
import com.closedcircuit.closedcircuitapplication.domain.model.Price
import com.closedcircuit.closedcircuitapplication.util.Empty

data class FundRequest(
    val id: ID,
    val beneficiaryId: ID,
    val planId: ID,
    val meansOfSupport: String,
    val minimumLoanRange: Price?,
    val maximumLoanRange: Price?,
    val maxLenders: Int?,
    val graceDuration: Int?,
    val repaymentDuration: Int?,
    val interestRate: Int?,
    val createdAt: Date,
    val updatedAt: Date
) {
    companion object {
        fun buildFundRequest(
            id: ID = ID.generateRandomUUID(),
            beneficiaryId: ID = ID.generateRandomUUID(),
            planId: ID = ID.generateRandomUUID(),
            meansOfSupport: String,
            minimumLoanRange: Price? = null,
            maximumLoanRange: Price? = null,
            maxLenders: Int? = null,
            graceDuration: Int? = null,
            repaymentDuration: Int? = null,
            interestRate: Int? = null,
            createdAt: Date = Date.now(),
            updatedAt: Date = Date.now()
        ): FundRequest {
            return FundRequest(
                id = id,
                beneficiaryId = beneficiaryId,
                planId = planId,
                meansOfSupport = meansOfSupport,
                minimumLoanRange = minimumLoanRange,
                maximumLoanRange = maximumLoanRange,
                maxLenders = maxLenders,
                graceDuration = graceDuration,
                repaymentDuration = repaymentDuration,
                interestRate = interestRate,
                createdAt = createdAt,
                updatedAt = updatedAt
            )
        }
    }
}
