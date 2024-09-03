package com.closedcircuit.closedcircuitapplication.beneficiary.data.payment

import com.closedcircuit.closedcircuitapplication.beneficiary.data.payment.dto.GeneratePaymentLinkRequest
import com.closedcircuit.closedcircuitapplication.core.network.ApiResponse
import com.closedcircuit.closedcircuitapplication.core.network.mapOnSuccess
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.payment.PaymentRepository
import com.closedcircuit.closedcircuitapplication.common.domain.model.Amount
import com.closedcircuit.closedcircuitapplication.common.domain.model.ID
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class PaymentRepositoryImpl(
    private val paymentService: PaymentService,
    private val ioDispatcher: CoroutineDispatcher,
) : PaymentRepository {

    override suspend fun generateTokenizationLink(): ApiResponse<String> {
        return withContext(ioDispatcher) {
            paymentService.tokenizeCard().mapOnSuccess { it.link }
        }
    }

    override suspend fun generatePaymentLink(loanID: ID, amount: Amount): ApiResponse<String> {
        val request = GeneratePaymentLinkRequest(
            loanId = loanID.value,
            category = "in",
            type = "wallet_top_up",
            amount = amount.value.toInt()
        )

        return withContext(ioDispatcher) {
            paymentService.generatePaymentLink(request).mapOnSuccess {
                it.link
            }
        }
    }
}