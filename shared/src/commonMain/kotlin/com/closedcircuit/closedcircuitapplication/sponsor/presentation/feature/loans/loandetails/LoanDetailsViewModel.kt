package com.closedcircuit.closedcircuitapplication.sponsor.presentation.feature.loans.loandetails

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.screenModelScope
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.payment.PaymentRepository
import com.closedcircuit.closedcircuitapplication.common.domain.model.ID
import com.closedcircuit.closedcircuitapplication.common.domain.model.LoanStatus
import com.closedcircuit.closedcircuitapplication.common.presentation.util.BaseScreenModel
import com.closedcircuit.closedcircuitapplication.core.network.onComplete
import com.closedcircuit.closedcircuitapplication.core.network.onError
import com.closedcircuit.closedcircuitapplication.core.network.onSuccess
import com.closedcircuit.closedcircuitapplication.sponsor.domain.loan.LoanRepository
import kotlinx.coroutines.launch

class LoanDetailsViewModel(
    private val id: ID,
    private val loanRepository: LoanRepository,
    private val paymentRepository: PaymentRepository
) : BaseScreenModel<LoanDetailsUiState, LoanDetailsResult>() {

    var state by mutableStateOf<LoanDetailsUiState>(LoanDetailsUiState.Loading)

    init {
        fetchLoanDetails()
    }

    fun onEvent(event: LoanDetailsUiEvent) {
        when (event) {
            LoanDetailsUiEvent.Cancel -> cancelLoanOffer()
            LoanDetailsUiEvent.InitiatePayment -> initiatePayment()
        }
    }

    private fun initiatePayment() {
        val contentState = (state as? LoanDetailsUiState.Content) ?: return
        state = contentState.copy(loading = true)
        screenModelScope.launch {
            paymentRepository.generatePaymentLink(loanID = id, amount = contentState.loanAmount)
                .onComplete {
                    state = contentState.copy(loading = false)
                }.onSuccess {
                    _resultChannel.send(LoanDetailsResult.InitiatePaymentSuccess(it))
                }.onError { _, message ->
                    _resultChannel.send(LoanDetailsResult.Error(message))
                }
        }
    }

    private fun fetchLoanDetails() {
        screenModelScope.launch {
            loanRepository.fetchLoanOfferDetails(id).onSuccess {
                val graceDuration = it.graceDuration
                val repaymentDuration = it.repaymentDuration
                val totalDuration = graceDuration + repaymentDuration
                state = LoanDetailsUiState.Content(
                    loading = false,
                    canInitiatePayment = it.status == LoanStatus.ACCEPTED,
                    canCancelOffer = it.status != LoanStatus.DECLINED && it.status != LoanStatus.PAID,
                    loanAmount = it.loanAmount,
                    formattedLoanAmount = it.loanAmount.getFormattedValue(),
                    interestAmount = it.interestAmount.getFormattedValue(),
                    repaymentAmount = it.repaymentAmount?.getFormattedValue().orEmpty(),
                    graceDuration = it.graceDuration.value.toString(),
                    repaymentDuration = it.repaymentDuration.value.toString(),
                    totalDuration = totalDuration.toString(),
                )
            }.onError { _, message ->
                state = LoanDetailsUiState.Error(message)
            }
        }
    }

    private fun cancelLoanOffer() {
        val contentState = (state as? LoanDetailsUiState.Content) ?: return
        state = contentState.copy(loading = true)
        screenModelScope.launch {
            loanRepository.cancelLoanOffer(id).onComplete {
                state = contentState.copy(loading = false)
            }.onSuccess {
                _resultChannel.send(LoanDetailsResult.CancelSuccess)
            }.onError { _, message ->
                _resultChannel.send(LoanDetailsResult.Error(message))
            }
        }
    }
}