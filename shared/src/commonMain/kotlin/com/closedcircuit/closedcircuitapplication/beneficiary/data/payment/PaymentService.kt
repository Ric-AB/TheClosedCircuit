package com.closedcircuit.closedcircuitapplication.beneficiary.data.payment

import com.closedcircuit.closedcircuitapplication.core.network.ApiResponse
import com.closedcircuit.closedcircuitapplication.beneficiary.data.payment.dto.TokenizeCardResponse
import com.closedcircuit.closedcircuitapplication.common.util.ClosedCircuitApiEndpoints.TOKENIZE_CARD
import com.closedcircuit.closedcircuitapplication.common.util.ClosedCircuitApiEndpoints.VERIFY_TRANSACTION_STATUS
import de.jensklingenberg.ktorfit.http.POST

interface PaymentService {

    @POST(VERIFY_TRANSACTION_STATUS)
    suspend fun verifyPaymentStatus()

    @POST(TOKENIZE_CARD)
    suspend fun tokenizeCard(): ApiResponse<TokenizeCardResponse>
}