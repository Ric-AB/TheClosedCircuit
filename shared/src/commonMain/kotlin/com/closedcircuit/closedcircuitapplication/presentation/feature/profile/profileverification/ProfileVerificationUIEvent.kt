package com.closedcircuit.closedcircuitapplication.presentation.feature.profile.profileverification

sealed interface ProfileVerificationUIEvent {

    data class OtpChange(val otp: String) : ProfileVerificationUIEvent
    data class RequestOtp(val isResend: Boolean) : ProfileVerificationUIEvent
    object Submit : ProfileVerificationUIEvent
}