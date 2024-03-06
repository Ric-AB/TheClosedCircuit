package com.closedcircuit.closedcircuitapplication.domain.payment

import com.closedcircuit.closedcircuitapplication.core.network.ApiResponse

interface PaymentRepository {

    suspend fun generateTokenizationLink(): ApiResponse<String>
}