package com.closedcircuit.closedcircuitapplication.presentation.feature.kyc

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.closedcircuit.closedcircuitapplication.core.network.onSuccess
import com.closedcircuit.closedcircuitapplication.domain.user.KycVerificationType
import com.closedcircuit.closedcircuitapplication.domain.user.UserRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.launch

class KycViewModel(
    private val userRepository: UserRepository
) : ScreenModel {

    var state by mutableStateOf(KycUiState.init(userRepository.userFlow.value))
        private set

    private val _kycResultChannel = Channel<KycResult>()
    val kycResultChannel: ReceiveChannel<KycResult> = _kycResultChannel


    fun onEvent(event: KycUiEvent) {
        when (event) {
            KycUiEvent.Submit -> attemptVerification()
            is KycUiEvent.VerificationNumberChange -> updateVerificationNumber(event.number)
            is KycUiEvent.VerificationTypeChange -> updateVerificationType(event.kycVerificationType)
        }
    }

    private fun attemptVerification() {
        if (isFieldValid()) {
            state = state?.copy(isLoading = true)

            val verificationType = state?.selectedVerificationType ?: return
            val verificationNumber = state?.verificationNumber?.value ?: return
            val userId = state?.user?.id?.value ?: return

            screenModelScope.launch {
                userRepository.verifyKyc(
                    verificationType = verificationType,
                    verificationNumber = verificationNumber
                ).onSuccess {
                    userRepository.fetchUser(userId)
                    state = state?.copy(isLoading = false)
                    _kycResultChannel.send(KycResult.Success)
                }
            }
        }
    }

    private fun updateVerificationNumber(number: String) {
        state?.verificationNumber?.onValueChange(number)
    }

    private fun updateVerificationType(type: KycVerificationType) {
        state = state?.copy(selectedVerificationType = type)
    }

    private fun isFieldValid(): Boolean {
        return state?.let {
            it.verificationNumber.validateInput()
            !it.verificationNumber.isError && it.selectedVerificationType != null
        } ?: false
    }
}