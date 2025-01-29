package com.closedcircuit.closedcircuitapplication.common.presentation.feature.profile.profileverification

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.closedcircuit.closedcircuitapplication.common.domain.auth.AuthenticationRepository
import com.closedcircuit.closedcircuitapplication.common.domain.model.Email
import com.closedcircuit.closedcircuitapplication.common.domain.user.UserRepository
import com.closedcircuit.closedcircuitapplication.core.network.onComplete
import com.closedcircuit.closedcircuitapplication.core.network.onError
import com.closedcircuit.closedcircuitapplication.core.network.onSuccess
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.launch

class EmailVerificationViewModel(
    private val email: Email,
    private val authenticationRepository: AuthenticationRepository,
    private val userRepository: UserRepository
) : ScreenModel {

    var state by mutableStateOf(EmailVerificationUIState())

    private val _resultChannel: Channel<EmailVerificationResult> = Channel()
    val resultChannel: ReceiveChannel<EmailVerificationResult> = _resultChannel

    init {
        requestOtp()
    }

    fun onEvent(event: EmailVerificationUiEvent) {
        when (event) {
            is EmailVerificationUiEvent.OtpChange -> updateOtp(event.otp)
            is EmailVerificationUiEvent.RequestOtp -> requestOtp(event.isResend)
            EmailVerificationUiEvent.Submit -> submitOtp()
        }
    }

    private fun updateOtp(otp: String) {
        state.otpCodeField.onValueChange(otp)
    }

    private fun requestOtp(resend: Boolean = false) {
        if (resend) state = state.copy(isLoading = true)

        screenModelScope.launch {
            authenticationRepository.requestOtp(email.value)
                .onComplete {
                    state = state.copy(isLoading = false)
                }.onSuccess {
                    if (resend)
                        _resultChannel.send(EmailVerificationResult.RequestOtpSuccess)
                }.onError { _, message ->
                    _resultChannel.send(EmailVerificationResult.RequestOtpFailure(message))
                }
        }
    }

    private fun submitOtp() {
        val otpCode = state.otpCodeField.value
        val email = email.value

        state = state.copy(isLoading = true)
        screenModelScope.launch {
            authenticationRepository.verifyOtp(otpCode = otpCode, email = email)
                .onSuccess {
                    refreshUser()
                }.onError { _, message ->
                    _resultChannel.send(EmailVerificationResult.VerifyOtpFailure(message))
                }
        }
    }

    private suspend fun refreshUser() {
        val user = userRepository.getCurrentUser()
        userRepository.fetchLoggedInUser(userId = user.id)
            .onComplete {
                state = state.copy(isLoading = false)
            }.onSuccess {
                _resultChannel.send(EmailVerificationResult.VerifyOtpSuccess)
            }.onError { _, message ->
                _resultChannel.send(EmailVerificationResult.VerifyOtpFailure(message))
            }
    }
}
