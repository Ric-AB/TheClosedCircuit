package com.closedcircuit.closedcircuitapplication.presentation.feature.loans.preview

import com.closedcircuit.closedcircuitapplication.domain.model.LoanStatus

sealed interface LoansPreviewUiEvent {
    data class Fetch(val loanStatus: LoanStatus) : LoansPreviewUiEvent
}