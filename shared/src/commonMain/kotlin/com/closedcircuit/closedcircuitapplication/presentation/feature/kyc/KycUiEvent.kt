package com.closedcircuit.closedcircuitapplication.presentation.feature.kyc

import com.closedcircuit.closedcircuitapplication.domain.user.KycVerificationType

sealed interface KycUiEvent {
    data class VerificationTypeChange(val kycVerificationType: KycVerificationType) : KycUiEvent
    data class VerificationNumberChange(val number: String) : KycUiEvent
    object Submit : KycUiEvent
}