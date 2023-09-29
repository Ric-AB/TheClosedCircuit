package com.closedcircuit.closedcircuitapplication.presentation.feature.authentication.passwordrecovery

sealed interface ResetPasswordUIEvent {
    data class EmailChange(val email: String) : ResetPasswordUIEvent
    data class OtpCodeChange(val otpCode: String) : ResetPasswordUIEvent
    data class PasswordChange(val password: String) : ResetPasswordUIEvent
    data class ConfirmPasswordChange(val confirmPassword: String) : ResetPasswordUIEvent
    data class RequestOtp(val isResend: Boolean = false) : ResetPasswordUIEvent
    object SubmitOtp : ResetPasswordUIEvent
    object SubmitPassword : ResetPasswordUIEvent
}