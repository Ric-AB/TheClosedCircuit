package com.closedcircuit.closedcircuitapplication.beneficiary.presentation.feature.loans.loanlist

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.screenModelScope
import com.closedcircuit.closedcircuitapplication.core.network.onError
import com.closedcircuit.closedcircuitapplication.core.network.onSuccess
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.loan.Loan
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.loan.LoanRepository
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.model.ID
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.model.LoanStatus
import com.closedcircuit.closedcircuitapplication.common.presentation.util.BaseScreenModel
import com.closedcircuit.closedcircuitapplication.common.util.replaceAll
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch

class LoansViewModel(private val loanRepository: LoanRepository) :
    BaseScreenModel<LoansUiState, Unit>() {

    private val loading = mutableStateOf(false)
    private val errorMessage = mutableStateOf<String?>(null)
    private val loans = mutableStateListOf<Loan>()

    @Composable
    override fun uiState(): LoansUiState {
        return when {
            loading.value -> LoansUiState.Loading
            !errorMessage.value.isNullOrEmpty() -> LoansUiState.Error(errorMessage.value!!)
            else -> LoansUiState.Content(loans.toImmutableList())
        }
    }

    fun onEvent(event: LoansUiEvent) {
        when (event) {
            is LoansUiEvent.Fetch -> fetchLoansBy(event.planId, event.loanStatus)
        }
    }

    private fun fetchLoansBy(planId: ID, loanStatus: LoanStatus) {
        screenModelScope.launch {
            loading.value = true
            errorMessage.value = null
            loanRepository.fetchLoansBy(planId, loanStatus).onSuccess {
                loans.replaceAll(it)
            }.onError { _, message ->
                errorMessage.value = message
            }

            loading.value = false
        }
    }
}