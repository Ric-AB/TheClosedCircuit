package com.closedcircuit.closedcircuitapplication.presentation.feature.authentication.password_recovery

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.closedcircuit.closedcircuitapplication.core.network.onError
import com.closedcircuit.closedcircuitapplication.core.network.onSuccess
import com.closedcircuit.closedcircuitapplication.domain.user.UserRepository
import kotlinx.coroutines.launch

class ResetPasswordViewModel(
    private val userRepository: UserRepository
) : ScreenModel {

    var state by mutableStateOf(ResetPasswordUIState())
        private set

    fun onEvent(event: ResetPasswordUIEvent) {
        when (event) {
            is ResetPasswordUIEvent.ConfirmPasswordChange -> updateConfirmPassword(event.confirmPassword)
            is ResetPasswordUIEvent.EmailChange -> updateEmail(event.email)
            is ResetPasswordUIEvent.OtpCodeChange -> updateOtpCode(event.otpCode)
            is ResetPasswordUIEvent.PasswordChange -> updatePassword(event.password)
            ResetPasswordUIEvent.SubmitEmail -> submitEmail()
            ResetPasswordUIEvent.SubmitOtp -> submitOtp()
            ResetPasswordUIEvent.SubmitPassword -> submitPassword()
        }
    }

    private fun submitEmail() {
        state = state.copy(loading = true)
        coroutineScope.launch {
            val email = state.email.lowercase().trim()
            userRepository.generateOtp(email)
                .onSuccess {
                    state = state.copy(loading = false)
                }.onError { _, message ->
                    state = state.copy(loading = false)
                }
        }
    }

    private fun submitOtp() {
        state = state.copy(loading = true)
        coroutineScope.launch {
            val email = state.email.lowercase().trim()
            userRepository.verifyOtp(otpCode = state.otpCode, email = email)
                .onSuccess {
                    state = state.copy(loading = false)
                }.onError { _, message ->
                    state = state.copy(loading = false)
                }
        }
    }

    private fun submitPassword() {
        state = state.copy(loading = true)
        coroutineScope.launch {
            val email = state.email.lowercase().trim()
            userRepository.resetPassword(
                otpCode = state.otpCode,
                email = email,
                password = state.password,
                confirmPassword = state.confirmPassword
            ).onSuccess {
                state = state.copy(loading = false)
            }.onError { _, message ->
                state = state.copy(loading = false)
            }
        }
    }

    private fun updateEmail(email: String) {
        state = state.copy(email = email)
    }

    private fun updateOtpCode(otp: String) {
        state = state.copy(otpCode = otp)
    }

    private fun updatePassword(password: String) {
        state = state.copy(password = password)
    }

    private fun updateConfirmPassword(confirmPassword: String) {
        state = state.copy(confirmPassword = confirmPassword)
    }
}