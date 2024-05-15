package com.closedcircuit.closedcircuitapplication.common.domain.fundrequest

import com.closedcircuit.closedcircuitapplication.beneficiary.domain.model.Date
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.model.FundType
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.model.ID
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.model.Amount
import kotlinx.serialization.Serializable

@Serializable
data class FundRequest(
    val id: ID,
    val beneficiaryId: ID?,
    val planId: ID?,
    val fundType: FundType,
    val minimumLoanRange: Amount?,
    val maximumLoanRange: Amount?,
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
            fundType: FundType,
            minimumLoanRange: Amount? = null,
            maximumLoanRange: Amount? = null,
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
                fundType = fundType,
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
