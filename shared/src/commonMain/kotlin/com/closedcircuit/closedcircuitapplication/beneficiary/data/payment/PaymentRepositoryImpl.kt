package com.closedcircuit.closedcircuitapplication.beneficiary.data.payment

import com.closedcircuit.closedcircuitapplication.core.network.ApiResponse
import com.closedcircuit.closedcircuitapplication.core.network.mapOnSuccess
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.payment.PaymentRepository
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
}