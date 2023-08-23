package com.closedcircuit.closedcircuitapplication.presentation.feature.authentication.password_recovery

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.closedcircuit.closedcircuitapplication.core.network.onError
import com.closedcircuit.closedcircuitapplication.core.network.onSuccess
import com.closedcircuit.closedcircuitapplication.domain.user.UserRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.launch

class ResetPasswordViewModel(
    private val userRepository: UserRepository
) : ScreenModel {

    var state by mutableStateOf(ResetPasswordUIState())
        private set

    private val _requestOtpResult = Channel<RequestOtpResult>()
    val requestOtpResult: ReceiveChannel<RequestOtpResult> = _requestOtpResult

    private val _verifyOtpResult = Channel<VerifyOtpResult>()
    val verifyOtpResult: ReceiveChannel<VerifyOtpResult> = _verifyOtpResult

    private val _resetPasswordResult = Channel<ResetPasswordResult>()
    val resetPasswordResult: ReceiveChannel<ResetPasswordResult> = _resetPasswordResult

    fun onEvent(event: ResetPasswordUIEvent) {
        when (event) {
            is ResetPasswordUIEvent.ConfirmPasswordChange -> updateConfirmPassword(event.confirmPassword)
            is ResetPasswordUIEvent.EmailChange -> updateEmail(event.email)
            is ResetPasswordUIEvent.OtpCodeChange -> updateOtpCode(event.otpCode)
            is ResetPasswordUIEvent.PasswordChange -> updatePassword(event.password)
            is ResetPasswordUIEvent.RequestOtp -> submitEmail(event.isResend)
            ResetPasswordUIEvent.SubmitOtp -> submitOtp()
            ResetPasswordUIEvent.SubmitPassword -> submitPassword()
        }
    }

    private fun submitEmail(resend: Boolean) {
        if (isEmailValid()) {
            val email = state.emailField.value.lowercase().trim()
            state = state.copy(loading = true)
            coroutineScope.launch {
                userRepository.requestOtp(email)
                    .onSuccess {
                        state = state.copy(loading = false)
                        if (resend) {
                            _requestOtpResult.send(RequestOtpResult.Success(true))
                        } else {
                            _requestOtpResult.send(RequestOtpResult.Success())
                        }
                    }.onError { _, message ->
                        state = state.copy(loading = false)
                        _requestOtpResult.send(RequestOtpResult.Failure(message))
                    }
            }
        }
    }

    private fun submitOtp() {
        val email = state.emailField.value.lowercase().trim()
        val otpCode = state.otpCodeField.value

        state = state.copy(loading = true)
        coroutineScope.launch {
            userRepository.verifyOtp(otpCode = otpCode, email = email)
                .onSuccess {
                    state = state.copy(loading = false)
                    _verifyOtpResult.send(VerifyOtpResult.Success)
                }.onError { _, message ->
                    state = state.copy(loading = false)
                    _verifyOtpResult.send(VerifyOtpResult.Failure(message))
                }
        }
    }

    private fun submitPassword() {
        val isValid = doPasswordsMatch()
        if (isValid) {
            val otpCode = state.otpCodeField.value
            val email = state.emailField.value.lowercase().trim()
            val password = state.passwordField.value
            val confirmPassword = state.confirmPasswordField.value

            state = state.copy(loading = true)
            coroutineScope.launch {
                userRepository.resetPassword(
                    otpCode = otpCode,
                    email = email,
                    password = password,
                    confirmPassword = confirmPassword
                ).onSuccess {
                    state = state.copy(loading = false)
                    _resetPasswordResult.send(ResetPasswordResult.Success)
                }.onError { _, message ->
                    state = state.copy(loading = false)
                    _resetPasswordResult.send(ResetPasswordResult.Failure(message))
                }
            }
        }
    }

    private fun updateEmail(email: String) {
        state.emailField.onValueChange(email)
    }

    private fun isEmailValid(): Boolean {
        val emailField = state.emailField
        val trimmedEmail = emailField.value.trim()
        emailField.onValueChange(trimmedEmail)
        emailField.validateInput()
        return emailField.error.isEmpty()
    }

    private fun updateOtpCode(otp: String) {
        state.otpCodeField.onValueChange(otp)
    }

    private fun updatePassword(password: String) {
        state.passwordField.onValueChange(password)
    }

    private fun updateConfirmPassword(confirmPassword: String) {
        state.confirmPasswordField.onValueChange(confirmPassword)
        validateConfirmPassword()
    }

    private fun doPasswordsMatch(): Boolean {
        validateConfirmPassword()
        return state.passwordField.value == state.confirmPasswordField.value
    }

    private fun validateConfirmPassword() {
        if (state.confirmPasswordField.value != state.passwordField.value) {
            state.confirmPasswordField.error = "Passwords do not match"
        } else {
            state.confirmPasswordField.error = ""
        }
    }
}