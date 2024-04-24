package com.closedcircuit.closedcircuitapplication.common.presentation.feature.authentication.passwordrecovery

import com.closedcircuit.closedcircuitapplication.common.util.validation.EmailValidator
import com.closedcircuit.closedcircuitapplication.common.util.validation.PasswordValidator
import com.closedcircuit.closedcircuitapplication.common.util.InputField

data class ResetPasswordUIState(
    val emailField: InputField = InputField(validator = EmailValidator()),
    val otpCodeField: InputField = InputField(),
    val passwordField: InputField = InputField(
        validator = PasswordValidator(),
        validateOnChange = true
    ),

    val confirmPasswordField: InputField = InputField(),
    val loading: Boolean = false,
)

sealed interface RequestOtpResult {
    data class Success(val isResend: Boolean = false) : RequestOtpResult
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
