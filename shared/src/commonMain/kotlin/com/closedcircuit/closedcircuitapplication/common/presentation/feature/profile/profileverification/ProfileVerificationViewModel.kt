package com.closedcircuit.closedcircuitapplication.common.presentation.feature.profile.profileverification

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.auth.AuthenticationRepository
import com.closedcircuit.closedcircuitapplication.common.domain.model.Email
import com.closedcircuit.closedcircuitapplication.core.network.onComplete
import com.closedcircuit.closedcircuitapplication.core.network.onError
import com.closedcircuit.closedcircuitapplication.core.network.onSuccess
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.launch

class ProfileVerificationViewModel(
    private val email: Email,
    private val authenticationRepository: AuthenticationRepository
) : ScreenModel {

    var state by mutableStateOf(ProfileVerificationUIState())

    private val _resultChannel: Channel<ProfileVerificationResult> = Channel()
    val resultChannel: ReceiveChannel<ProfileVerificationResult> = _resultChannel

    init {
        requestOtp()
    }

    fun onEvent(event: ProfileVerificationUiEvent) {
        when (event) {
            is ProfileVerificationUiEvent.OtpChange -> updateOtp(event.otp)
            is ProfileVerificationUiEvent.RequestOtp -> requestOtp(event.isResend)
            ProfileVerificationUiEvent.Submit -> submitOtp()
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
                        _resultChannel.send(ProfileVerificationResult.RequestOtpSuccess)
                }.onError { _, message ->
                    _resultChannel.send(ProfileVerificationResult.RequestOtpFailure(message))
                }
        }
    }

    private fun submitOtp() {
        val otpCode = state.otpCodeField.value
        val email = email.value

        state = state.copy(isLoading = true)
        screenModelScope.launch {
            authenticationRepository.verifyOtp(otpCode = otpCode, email = email)
                .onComplete {
                    state = state.copy(isLoading = false)
                }.onSuccess {
                    _resultChannel.send(ProfileVerificationResult.VerifyOtpSuccess)
                }.onError { _, message ->
                    _resultChannel.send(ProfileVerificationResult.VerifyOtpFailure(message))
                }
        }
    }
}
