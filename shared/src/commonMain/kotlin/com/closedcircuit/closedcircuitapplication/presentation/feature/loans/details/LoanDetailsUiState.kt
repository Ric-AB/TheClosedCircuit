package com.closedcircuit.closedcircuitapplication.presentation.feature.loans.details

import com.closedcircuit.closedcircuitapplication.domain.loan.LoanSchedule
import kotlinx.collections.immutable.ImmutableList

sealed interface LoanDetailsUiState {
    object Loading : LoanDetailsUiState
    data class DataLoaded(
        val loading: Boolean,
        val canTransact: Boolean,
        val loanAmount: String,
        val interestAmount: String,
        val repaymentAmount: String,
        val repaymentSchedule: ImmutableList<LoanSchedule>
    ) : LoanDetailsUiState

    data class Error(val message: String) : LoanDetailsUiState

    val postLoading: Boolean
        get() {
            return this is DataLoaded && this.loading
        }
}


sealed interface LoanDetailsResult {
    object Success : LoanDetailsResult

    data class Error(val message: String) : LoanDetailsResult
}