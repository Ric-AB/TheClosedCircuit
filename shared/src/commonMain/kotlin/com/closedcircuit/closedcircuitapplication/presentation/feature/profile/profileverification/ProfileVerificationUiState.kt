package com.closedcircuit.closedcircuitapplication.presentation.feature.profile.profileverification

import com.closedcircuit.closedcircuitapplication.domain.model.Email
import com.closedcircuit.closedcircuitapplication.util.InputField

data class ProfileVerificationUIState(
    val email: Email,
    val otpCodeField: InputField = InputField(),
    val isLoading: Boolean = false
)

sealed interface ProfileVerificationResult {
    object RequestOtpSuccess : ProfileVerificationResult
    data class RequestOtpFailure(val message: String) : ProfileVerificationResult
    object VerifyOtpSuccess : ProfileVerificationResult
    data class VerifyOtpFailure(val message: String) : ProfileVerificationResult
}
