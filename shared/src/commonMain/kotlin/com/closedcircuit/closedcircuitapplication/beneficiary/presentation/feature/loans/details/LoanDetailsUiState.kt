package com.closedcircuit.closedcircuitapplication.beneficiary.presentation.feature.loans.details

import com.closedcircuit.closedcircuitapplication.beneficiary.domain.loan.LoanSchedule
import kotlinx.collections.immutable.ImmutableList

sealed interface LoanDetailsUiState {
    object Loading : LoanDetailsUiState
    data class Content(
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
            return this is Content && this.loading
        }
}


sealed interface LoanDetailsResult {
    object Success : LoanDetailsResult

    data class Error(val message: String) : LoanDetailsResult
}