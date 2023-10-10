package com.closedcircuit.closedcircuitapplication.presentation.feature.profile.profileverification

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.closedcircuit.closedcircuitapplication.core.network.onError
import com.closedcircuit.closedcircuitapplication.core.network.onSuccess
import com.closedcircuit.closedcircuitapplication.domain.auth.AuthenticationRepository
import com.closedcircuit.closedcircuitapplication.domain.model.Email
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.launch

class ProfileVerificationViewModel(
    private val email: Email,
    private val authenticationRepository: AuthenticationRepository
) : ScreenModel {

    var state by mutableStateOf(ProfileVerificationUIState(email = email))

    private val _resultChannel: Channel<ProfileVerificationResult> = Channel()
    val resultChannel: ReceiveChannel<ProfileVerificationResult> = _resultChannel

    init {
        requestOtp()
    }

    fun onEvent(event: ProfileVerificationUIEvent) {
        when (event) {
            is ProfileVerificationUIEvent.OtpChange -> updateOtp(event.otp)
            is ProfileVerificationUIEvent.RequestOtp -> requestOtp(event.isResend)
            ProfileVerificationUIEvent.Submit -> submitOtp()
        }
    }

    private fun updateOtp(otp: String) {
        state.otpCodeField.onValueChange(otp)
    }

    private fun requestOtp(resend: Boolean = false) {
        if (resend) state = state.copy(isLoading = true)

        coroutineScope.launch {
            authenticationRepository.requestOtp(email.value)
                .onSuccess {
                    state = state.copy(isLoading = false)

                    if (resend)
                        _resultChannel.send(ProfileVerificationResult.RequestOtpSuccess)
                }.onError { _, message ->
                    state = state.copy(isLoading = false)
                    _resultChannel.send(ProfileVerificationResult.RequestOtpFailure(message))
                }
        }
    }

    private fun submitOtp() {
        val otpCode = state.otpCodeField.value
        val email = email.value

        state = state.copy(isLoading = true)
        coroutineScope.launch {
            authenticationRepository.verifyOtp(otpCode = otpCode, email = email)
                .onSuccess {
                    state = state.copy(isLoading = false)
                    _resultChannel.send(ProfileVerificationResult.VerifyOtpSuccess)
                }.onError { _, message ->
                    state = state.copy(isLoading = false)
                    _resultChannel.send(ProfileVerificationResult.VerifyOtpFailure(message))
                }
        }
    }
}
