package com.closedcircuit.closedcircuitapplication.presentation.feature.payment

sealed interface PaymentResult {
    object Success : PaymentResult
    data class Error(val message: String) : PaymentResult
}