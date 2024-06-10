package com.closedcircuit.closedcircuitapplication.sponsor.presentation.feature.loans.loandetails

import com.closedcircuit.closedcircuitapplication.common.domain.loan.LoanSchedule
import kotlinx.collections.immutable.ImmutableList

sealed interface LoanDetailsUiState {
    object Loading : LoanDetailsUiState
    data class Content(
        val loading: Boolean,
        val canInitiatePayment: Boolean,
        val canCancelOffer: Boolean,
        val loanAmount: String,
        val interestAmount: String,
        val repaymentAmount: String,
        val graceDuration: String,
        val repaymentDuration: String,
        val totalDuration: String,
    ) : LoanDetailsUiState

    data class Error(val message: String) : LoanDetailsUiState

    val postLoading: Boolean
        get() {
            return this is Content && this.loading
        }
}


sealed interface LoanDetailsResult {
    object CancelSuccess : LoanDetailsResult
    data class InitiatePaymentSuccess(val paymentLink: String) : LoanDetailsResult
    data class Error(val message: String) : LoanDetailsResult
}