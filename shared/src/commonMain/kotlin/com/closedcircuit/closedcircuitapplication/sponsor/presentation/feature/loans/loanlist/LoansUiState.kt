package com.closedcircuit.closedcircuitapplication.sponsor.presentation.feature.loans.loanlist

import com.closedcircuit.closedcircuitapplication.sponsor.domain.loan.LoanOffer
import kotlinx.collections.immutable.ImmutableList

sealed interface LoansUiState {
    object Loading : LoansUiState

    object Empty: LoansUiState

    data class Content(val items: ImmutableList<LoanOffer>) : LoansUiState

    data class Error(val message: String) : LoansUiState
}