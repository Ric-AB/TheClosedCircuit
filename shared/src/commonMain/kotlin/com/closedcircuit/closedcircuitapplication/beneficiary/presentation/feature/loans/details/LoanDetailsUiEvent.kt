package com.closedcircuit.closedcircuitapplication.beneficiary.presentation.feature.loans.details

import com.closedcircuit.closedcircuitapplication.common.domain.model.LoanStatus

sealed interface LoanDetailsUiEvent {
    object Fetch : LoanDetailsUiEvent

    data class Submit(val status: LoanStatus) : LoanDetailsUiEvent
}