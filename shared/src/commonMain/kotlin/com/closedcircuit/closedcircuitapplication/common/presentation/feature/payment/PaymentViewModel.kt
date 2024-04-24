package com.closedcircuit.closedcircuitapplication.common.presentation.feature.payment

import cafe.adriel.voyager.core.model.screenModelScope
import com.closedcircuit.closedcircuitapplication.common.presentation.util.BaseScreenModel
import kotlinx.coroutines.launch

class PaymentViewModel : BaseScreenModel<Unit, PaymentResult>() {

    fun onEvent(event: PaymentUiEvent) {
        when (event) {
            is PaymentUiEvent.UrlChange -> checkUrl(event.url)
        }
    }

    private fun checkUrl(url: String) {
        val hostUrl = "theclosedcircuit.info"
        if (url.contains(hostUrl, true)) {
            val isPaymentSuccessful = url.findParameterValue("status")
                ?.contains("success", ignoreCase = true) ?: false

            if (isPaymentSuccessful) {
                screenModelScope.launch {
                    _resultChannel.send(PaymentResult.Success)
                }
            }
        }
    }

    private fun String.findParameterValue(parameterName: String): String? {
        return this.split('&').map {
            val parts = it.split('=')
            val name = parts.firstOrNull() ?: ""
            val value = parts.drop(1).firstOrNull() ?: ""
            Pair(name, value)
        }.firstOrNull { it.first == parameterName }?.second
    }
}