package com.closedcircuit.closedcircuitapplication.common.presentation.feature.payment

sealed interface PaymentUiEvent {
    data class UrlChange(val url: String) : PaymentUiEvent
}