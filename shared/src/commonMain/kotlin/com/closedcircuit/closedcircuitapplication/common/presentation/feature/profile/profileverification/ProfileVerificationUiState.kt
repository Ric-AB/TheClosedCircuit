package com.closedcircuit.closedcircuitapplication.common.presentation.feature.profile.profileverification

import com.closedcircuit.closedcircuitapplication.common.presentation.util.InputField

data class ProfileVerificationUIState(
    val otpCodeField: InputField = InputField(),
    val isLoading: Boolean = false
)

sealed interface ProfileVerificationResult {
    object RequestOtpSuccess : ProfileVerificationResult
    data class RequestOtpFailure(val message: String) : ProfileVerificationResult
    object VerifyOtpSuccess : ProfileVerificationResult
    data class VerifyOtpFailure(val message: String) : ProfileVerificationResult
}
