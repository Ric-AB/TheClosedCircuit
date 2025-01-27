package com.closedcircuit.closedcircuitapplication.common.presentation.feature.profile.profileverification

sealed interface EmailVerificationUiEvent {

    data class OtpChange(val otp: String) : EmailVerificationUiEvent
    data class RequestOtp(val isResend: Boolean) : EmailVerificationUiEvent
    object Submit : EmailVerificationUiEvent
}