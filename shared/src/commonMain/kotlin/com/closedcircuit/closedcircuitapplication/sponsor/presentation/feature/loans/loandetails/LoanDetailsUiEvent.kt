package com.closedcircuit.closedcircuitapplication.sponsor.presentation.feature.loans.loandetails

sealed interface LoanDetailsUiEvent {
    object InitiatePayment : LoanDetailsUiEvent
    object Cancel : LoanDetailsUiEvent
}