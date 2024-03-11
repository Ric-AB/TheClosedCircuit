package com.closedcircuit.closedcircuitapplication.presentation.feature.loans.loanlist

import com.closedcircuit.closedcircuitapplication.domain.model.ID
import com.closedcircuit.closedcircuitapplication.domain.model.LoanStatus

sealed interface LoansUiEvent {
    data class Fetch(val planId: ID, val loanStatus: LoanStatus) : LoansUiEvent
}