package com.closedcircuit.closedcircuitapplication.common.presentation.feature.profile.profileverification

sealed interface ProfileVerificationUiEvent {

    data class OtpChange(val otp: String) : ProfileVerificationUiEvent
    data class RequestOtp(val isResend: Boolean) : ProfileVerificationUiEvent
    object Submit : ProfileVerificationUiEvent
}