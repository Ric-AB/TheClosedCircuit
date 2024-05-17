package com.closedcircuit.closedcircuitapplication.beneficiary.presentation.feature.loans.loanlist

import com.closedcircuit.closedcircuitapplication.common.domain.model.ID
import com.closedcircuit.closedcircuitapplication.common.domain.model.LoanStatus

sealed interface LoansUiEvent {
    data class Fetch(val planId: ID, val loanStatus: LoanStatus) : LoansUiEvent
}