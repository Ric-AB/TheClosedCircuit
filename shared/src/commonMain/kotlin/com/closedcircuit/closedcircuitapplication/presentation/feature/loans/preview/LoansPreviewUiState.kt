package com.closedcircuit.closedcircuitapplication.presentation.feature.loans.preview

import com.closedcircuit.closedcircuitapplication.domain.loan.LoanPreview
import kotlinx.collections.immutable.ImmutableList


sealed interface LoansPreviewUiState {
    object Loading : LoansPreviewUiState
    data class Error(val message: String) : LoansPreviewUiState
    data class Content(val items: ImmutableList<LoanPreview>) : LoansPreviewUiState

}

