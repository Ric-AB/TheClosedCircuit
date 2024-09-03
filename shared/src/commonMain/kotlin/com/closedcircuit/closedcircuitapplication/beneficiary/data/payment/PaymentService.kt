package com.closedcircuit.closedcircuitapplication.beneficiary.data.payment

import com.closedcircuit.closedcircuitapplication.beneficiary.data.payment.dto.GeneratePaymentLinkRequest
import com.closedcircuit.closedcircuitapplication.core.network.ApiResponse
import com.closedcircuit.closedcircuitapplication.beneficiary.data.payment.dto.PaymentLinkResponse
import com.closedcircuit.closedcircuitapplication.common.data.util.ClosedCircuitApiEndpoints.CHARGE
import com.closedcircuit.closedcircuitapplication.common.data.util.ClosedCircuitApiEndpoints.TOKENIZE_CARD
import com.closedcircuit.closedcircuitapplication.common.data.util.ClosedCircuitApiEndpoints.VERIFY_TRANSACTION_STATUS
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.Headers
import de.jensklingenberg.ktorfit.http.POST

interface PaymentService {

    @POST(VERIFY_TRANSACTION_STATUS)
    suspend fun verifyPaymentStatus()

    @POST(TOKENIZE_CARD)
    suspend fun tokenizeCard(): ApiResponse<PaymentLinkResponse>

    @Headers("Content-Type: application/json")
    @POST(CHARGE)
    suspend fun generatePaymentLink(@Body request: GeneratePaymentLinkRequest): ApiResponse<PaymentLinkResponse>
}