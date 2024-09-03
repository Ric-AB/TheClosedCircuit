package com.closedcircuit.closedcircuitapplication.beneficiary.domain.payment

import com.closedcircuit.closedcircuitapplication.common.domain.model.Amount
import com.closedcircuit.closedcircuitapplication.common.domain.model.ID
import com.closedcircuit.closedcircuitapplication.core.network.ApiResponse

interface PaymentRepository {

    suspend fun generateTokenizationLink(): ApiResponse<String>

    suspend fun generatePaymentLink(loanID: ID, amount: Amount): ApiResponse<String>
}