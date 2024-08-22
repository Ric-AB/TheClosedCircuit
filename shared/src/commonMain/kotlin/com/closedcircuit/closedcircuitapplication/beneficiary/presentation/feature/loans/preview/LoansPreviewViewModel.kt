package com.closedcircuit.closedcircuitapplication.beneficiary.presentation.feature.loans.preview

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.screenModelScope
import com.closedcircuit.closedcircuitapplication.core.network.onError
import com.closedcircuit.closedcircuitapplication.core.network.onSuccess
import com.closedcircuit.closedcircuitapplication.common.domain.loan.LoanPreview
import com.closedcircuit.closedcircuitapplication.common.domain.loan.LoanRepository
import com.closedcircuit.closedcircuitapplication.common.domain.model.LoanStatus
import com.closedcircuit.closedcircuitapplication.common.presentation.util.BaseScreenModel
import com.closedcircuit.closedcircuitapplication.common.util.replaceAll
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch

class LoansPreviewViewModel(private val loanRepository: LoanRepository) :
    BaseScreenModel<LoansPreviewUiState, Unit>() {

    private val loading = mutableStateOf(false)
    private val errorMessage = mutableStateOf<String?>(null)
    private val loansPreviews = mutableStateListOf<LoanPreview>()

    @Composable
    override fun uiState(): LoansPreviewUiState {
        return when {
            loading.value -> LoansPreviewUiState.Loading
            !errorMessage.value.isNullOrEmpty() -> LoansPreviewUiState.Error(errorMessage.value!!)
            loansPreviews.isEmpty() -> LoansPreviewUiState.Empty
            else -> LoansPreviewUiState.Content(loansPreviews.toImmutableList())
        }
    }

    fun onEvent(event: LoansPreviewUiEvent) {
        when (event) {
            is LoansPreviewUiEvent.Fetch -> fetchLoansPreview(event.loanStatus)
        }
    }

    private fun fetchLoansPreview(loanStatus: LoanStatus) {
        screenModelScope.launch {
            loading.value = true
            errorMessage.value = null
            loanRepository.fetchLoanPreviews(loanStatus).onSuccess {
                loansPreviews.replaceAll(it)
            }.onError { _, message ->
                errorMessage.value = message
            }

            loading.value = false
        }
    }
}