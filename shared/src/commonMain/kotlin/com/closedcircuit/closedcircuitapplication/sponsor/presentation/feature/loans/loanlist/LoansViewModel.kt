package com.closedcircuit.closedcircuitapplication.sponsor.presentation.feature.loans.loanlist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.closedcircuit.closedcircuitapplication.common.domain.model.LoanStatus
import com.closedcircuit.closedcircuitapplication.core.network.onError
import com.closedcircuit.closedcircuitapplication.core.network.onSuccess
import com.closedcircuit.closedcircuitapplication.sponsor.domain.loan.LoanRepository
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch

class LoansViewModel(
    private val loanStatus: LoanStatus,
    private val loanRepository: LoanRepository
) : ScreenModel {

    var state by mutableStateOf<LoansUiState>(LoansUiState.Loading)

    init {
        fetchLoanOffers()
    }

    private fun fetchLoanOffers() {
        screenModelScope.launch {
            loanRepository.fetchLoanOffers(loanStatus).onSuccess {
                state = if (it.isEmpty()) LoansUiState.Empty
                else LoansUiState.Content(it.toImmutableList())
            }.onError { _, message ->
                state = LoansUiState.Error(message)
            }
        }
    }
}