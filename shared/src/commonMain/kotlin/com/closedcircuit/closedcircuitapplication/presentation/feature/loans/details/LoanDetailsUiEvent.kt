package com.closedcircuit.closedcircuitapplication.presentation.feature.loans.details

import com.closedcircuit.closedcircuitapplication.domain.model.LoanStatus

sealed interface LoanDetailsUiEvent {
    object Fetch : LoanDetailsUiEvent

    data class Submit(val status: LoanStatus) : LoanDetailsUiEvent
}