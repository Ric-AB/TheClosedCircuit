package com.closedcircuit.closedcircuitapplication.sponsor.presentation.feature.loans.loandetails

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.closedcircuit.closedcircuitapplication.common.domain.model.ID
import com.closedcircuit.closedcircuitapplication.core.network.onError
import com.closedcircuit.closedcircuitapplication.core.network.onSuccess
import com.closedcircuit.closedcircuitapplication.sponsor.domain.loan.LoanRepository
import kotlinx.coroutines.launch

class LoanDetailsViewModel(
    private val id: ID,
    private val loanRepository: LoanRepository
) : ScreenModel {

    var state by mutableStateOf<LoanDetailsUiState>(LoanDetailsUiState.Loading)

    init {
        fetchLoanDetails()
    }

    fun onEvent(event: LoanDetailsUiEvent) {
        when (event) {
            is LoanDetailsUiEvent.Cancel -> cancelLoanOffer()
        }
    }

    private fun fetchLoanDetails() {
        screenModelScope.launch {
            loanRepository.fetchLoanOfferDetails(id).onSuccess {

            }.onError { _, message ->
                state = LoanDetailsUiState.Error(message)
            }
        }
    }

    private fun cancelLoanOffer() {
        screenModelScope.launch {

        }
    }
}