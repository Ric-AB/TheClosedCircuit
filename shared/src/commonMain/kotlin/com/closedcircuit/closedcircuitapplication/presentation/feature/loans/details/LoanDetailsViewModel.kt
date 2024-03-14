package com.closedcircuit.closedcircuitapplication.presentation.feature.loans.details

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.screenModelScope
import com.closedcircuit.closedcircuitapplication.core.network.onError
import com.closedcircuit.closedcircuitapplication.core.network.onSuccess
import com.closedcircuit.closedcircuitapplication.domain.loan.LoanRepository
import com.closedcircuit.closedcircuitapplication.domain.loan.LoanSchedule
import com.closedcircuit.closedcircuitapplication.domain.model.ID
import com.closedcircuit.closedcircuitapplication.domain.model.LoanStatus
import com.closedcircuit.closedcircuitapplication.presentation.util.BaseScreenModel
import com.closedcircuit.closedcircuitapplication.util.Empty
import com.closedcircuit.closedcircuitapplication.util.replaceAll
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch

class LoanDetailsViewModel(
    private val loanID: ID,
    private val loanRepository: LoanRepository
) : BaseScreenModel<LoanDetailsUiState, LoanDetailsResult>() {

    private val errorMessage = mutableStateOf<String?>(null)
    private val loanStatus = mutableStateOf<LoanStatus?>(null)
    private val fetchLoading = mutableStateOf(false)
    private val postLoading = mutableStateOf(false)
    private val loanAmount = mutableStateOf(String.Empty)
    private val interestAmount = mutableStateOf(String.Empty)
    private val repaymentAmount = mutableStateOf(String.Empty)
    private val repaymentSchedule = mutableStateListOf<LoanSchedule>()

    @Composable
    override fun uiState(): LoanDetailsUiState {
        return when {
            fetchLoading.value -> LoanDetailsUiState.Loading
            errorMessage.value != null -> LoanDetailsUiState.Error(errorMessage.value!!)
            else -> LoanDetailsUiState.DataLoaded(
                loading = postLoading.value,
                canTransact = loanStatus.value == LoanStatus.PENDING,
                loanAmount = loanAmount.value,
                interestAmount = interestAmount.value,
                repaymentAmount = repaymentAmount.value,
                repaymentSchedule = repaymentSchedule.toImmutableList()
            )
        }
    }

    fun onEvent(event: LoanDetailsUiEvent) {
        when (event) {
            LoanDetailsUiEvent.Fetch -> fetchLoan()
            is LoanDetailsUiEvent.Submit -> acknowledgeLoan(event.status)
        }
    }

    private fun fetchLoan() {
        screenModelScope.launch {
            errorMessage.value = null
            fetchLoading.value = true
            loanRepository.fetchLoan(loanID).onSuccess {
                loanStatus.value = it.status
                loanAmount.value = it.loanAmount.value.toString()
                interestAmount.value = it.interestAmount.value.toString()
                repaymentAmount.value = it.repaymentAmount.value.toString()
                repaymentSchedule.replaceAll(it.loanSchedule.orEmpty())
            }.onError { _, message ->
                errorMessage.value = message
            }

            fetchLoading.value = false
        }
    }

    private fun acknowledgeLoan(loanStatus: LoanStatus) {
        screenModelScope.launch {
            postLoading.value = true
            loanRepository.acknowledgeLoan(loanID, loanStatus).onSuccess {
                _resultChannel.send(LoanDetailsResult.Success)
            }.onError { _, message ->
                _resultChannel.send(LoanDetailsResult.Error(message))
            }

            postLoading.value = false
        }
    }
}