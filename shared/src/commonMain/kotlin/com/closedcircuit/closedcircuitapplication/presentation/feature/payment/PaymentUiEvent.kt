package com.closedcircuit.closedcircuitapplication.presentation.feature.payment

sealed interface PaymentUiEvent {
    data class UrlChange(val url: String) : PaymentUiEvent
}