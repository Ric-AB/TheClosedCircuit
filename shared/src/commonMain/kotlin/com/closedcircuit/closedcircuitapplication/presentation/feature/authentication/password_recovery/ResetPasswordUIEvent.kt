package com.closedcircuit.closedcircuitapplication.presentation.feature.authentication.password_recovery

sealed interface ResetPasswordUIEvent {
    data class EmailChange(val email: String) : ResetPasswordUIEvent
    data class OtpCodeChange(val otpCode: String) : ResetPasswordUIEvent
    data class PasswordChange(val password: String) : ResetPasswordUIEvent
    data class ConfirmPasswordChange(val confirmPassword: String) : ResetPasswordUIEvent
    object SubmitEmail : ResetPasswordUIEvent
    object SubmitOtp : ResetPasswordUIEvent
    object SubmitPassword : ResetPasswordUIEvent
}