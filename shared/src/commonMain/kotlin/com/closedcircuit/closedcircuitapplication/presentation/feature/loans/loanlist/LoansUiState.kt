package com.closedcircuit.closedcircuitapplication.presentation.feature.loans.loanlist

import com.closedcircuit.closedcircuitapplication.domain.loan.Loan
import kotlinx.collections.immutable.ImmutableList

sealed interface LoansUiState {
    object Loading : LoansUiState

    data class Content(val items: ImmutableList<Loan>) : LoansUiState

    data class Error(val message: String) : LoansUiState
}
