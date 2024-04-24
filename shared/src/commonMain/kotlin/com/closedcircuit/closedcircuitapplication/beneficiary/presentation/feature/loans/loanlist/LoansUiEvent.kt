package com.closedcircuit.closedcircuitapplication.beneficiary.presentation.feature.loans.loanlist

import com.closedcircuit.closedcircuitapplication.beneficiary.domain.model.ID
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.model.LoanStatus

sealed interface LoansUiEvent {
    data class Fetch(val planId: ID, val loanStatus: LoanStatus) : LoansUiEvent
}