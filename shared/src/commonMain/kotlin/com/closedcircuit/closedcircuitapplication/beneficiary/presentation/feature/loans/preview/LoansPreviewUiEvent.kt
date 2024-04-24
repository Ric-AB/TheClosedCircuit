package com.closedcircuit.closedcircuitapplication.beneficiary.presentation.feature.loans.preview

import com.closedcircuit.closedcircuitapplication.beneficiary.domain.model.LoanStatus

sealed interface LoansPreviewUiEvent {
    data class Fetch(val loanStatus: LoanStatus) : LoansPreviewUiEvent
}