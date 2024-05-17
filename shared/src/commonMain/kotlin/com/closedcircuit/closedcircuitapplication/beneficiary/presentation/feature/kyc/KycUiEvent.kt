package com.closedcircuit.closedcircuitapplication.beneficiary.presentation.feature.kyc

import com.closedcircuit.closedcircuitapplication.common.domain.model.KycDocumentType

sealed interface KycUiEvent {
    data class DocumentTypeChange(val documentType: KycDocumentType) : KycUiEvent
    data class DocumentNumberChange(val number: String) : KycUiEvent
    object Submit : KycUiEvent
}