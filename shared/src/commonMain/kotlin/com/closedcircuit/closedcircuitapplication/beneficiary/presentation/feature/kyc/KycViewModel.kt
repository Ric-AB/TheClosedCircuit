package com.closedcircuit.closedcircuitapplication.beneficiary.presentation.feature.kyc

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.screenModelScope
import com.closedcircuit.closedcircuitapplication.core.network.onError
import com.closedcircuit.closedcircuitapplication.core.network.onSuccess
import com.closedcircuit.closedcircuitapplication.common.domain.model.KycDocumentType
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.user.UserRepository
import com.closedcircuit.closedcircuitapplication.common.presentation.util.BaseScreenModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class KycViewModel(
    private val userRepository: UserRepository
) : BaseScreenModel<KycUiState, KycResult>() {

    val user = userRepository.userFlow.value
    var state by mutableStateOf(KycUiState.init(user))
        private set


    fun onEvent(event: KycUiEvent) {
        when (event) {
            KycUiEvent.Submit -> attemptVerification()
            is KycUiEvent.DocumentNumberChange -> updateDocumentNumber(event.number)
            is KycUiEvent.DocumentTypeChange -> updateDocumentType(event.documentType)
        }
    }

    private fun attemptVerification() {
        if (isFieldValid()) {
            state = state?.copy(isLoading = true)

            val documentType = state?.selectedDocumentType ?: return
            val documentNumber = state?.documentNumber?.value ?: return

            screenModelScope.launch {
                userRepository.verifyKyc(
                    documentType = documentType,
                    documentNumber = documentNumber
                ).onSuccess {
                    checkKycStatus()
                }.onError { _, message ->
                    state = state?.copy(isLoading = false)
                    _resultChannel.send(KycResult.Failure(message))
                }
            }
        }
    }

    private fun checkKycStatus() {
        val delayInterval = 8.seconds

        screenModelScope.launch {
            delay(delayInterval)
            userRepository.fetchUser(user?.id?.value.orEmpty())
                .onSuccess {
                    state = state?.copy(
                        kycStatus = it.kycStatus,
                        phoneStatus = it.phoneNumberStatus
                    )

                    _resultChannel.send(KycResult.Success)
                }.onError { _, message ->
                    _resultChannel.send(KycResult.Failure(message))
                }

            state = state?.copy(isLoading = false)
        }
    }

    private fun updateDocumentNumber(number: String) {
        state?.documentNumber?.onValueChange(number)
    }

    private fun updateDocumentType(type: KycDocumentType) {
        state = state?.copy(selectedDocumentType = type)
    }

    private fun isFieldValid(): Boolean {
        return state?.let {
            it.documentNumber.validateInput()
            !it.documentNumber.isError && it.selectedDocumentType != null
        } ?: false
    }
}