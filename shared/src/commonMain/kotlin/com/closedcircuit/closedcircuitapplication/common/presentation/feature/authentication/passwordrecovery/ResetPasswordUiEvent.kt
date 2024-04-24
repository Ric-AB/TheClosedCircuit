package com.closedcircuit.closedcircuitapplication.common.presentation.feature.authentication.passwordrecovery

sealed interface ResetPasswordUiEvent {
    data class EmailChange(val email: String) : ResetPasswordUiEvent
    data class OtpCodeChange(val otpCode: String) : ResetPasswordUiEvent
    data class PasswordChange(val password: String) : ResetPasswordUiEvent
    data class ConfirmPasswordChange(val confirmPassword: String) : ResetPasswordUiEvent
    data class RequestOtp(val isResend: Boolean = false) : ResetPasswordUiEvent
    object SubmitOtp : ResetPasswordUiEvent
    object SubmitPassword : ResetPasswordUiEvent
}