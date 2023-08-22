package com.closedcircuit.closedcircuitapplication.presentation.feature.authentication.password_recovery

data class ResetPasswordUIState(
    val email: String = "",
    val otpCode: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val loading: Boolean = false,
)

sealed interface RequestOtpResult {
    object Success : RequestOtpResult
    data class Failure(val message: String) : RequestOtpResult
}

sealed interface VerifyOtpResult {
    object Success : VerifyOtpResult
    data class Failure(val message: String) : VerifyOtpResult
}

sealed interface ResetPasswordResult {
    object Success : ResetPasswordResult
    data class Failure(val message: String) : ResetPasswordResult
}
