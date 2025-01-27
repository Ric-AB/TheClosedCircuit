package com.closedcircuit.closedcircuitapplication.common.presentation.feature.profile.profileverification

import com.closedcircuit.closedcircuitapplication.common.presentation.util.InputField

data class EmailVerificationUIState(
    val otpCodeField: InputField = InputField(),
    val isLoading: Boolean = false
)

sealed interface EmailVerificationResult {
    object RequestOtpSuccess : EmailVerificationResult
    data class RequestOtpFailure(val message: String) : EmailVerificationResult
    object VerifyOtpSuccess : EmailVerificationResult
    data class VerifyOtpFailure(val message: String) : EmailVerificationResult
}
