package com.closedcircuit.closedcircuitapplication.presentation.feature.planmanagement.fundrequest

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.screenModelScope
import com.closedcircuit.closedcircuitapplication.core.network.onError
import com.closedcircuit.closedcircuitapplication.core.network.onSuccess
import com.closedcircuit.closedcircuitapplication.domain.fundrequest.FundRequest
import com.closedcircuit.closedcircuitapplication.domain.fundrequest.FundRequestRepository
import com.closedcircuit.closedcircuitapplication.domain.model.FundType
import com.closedcircuit.closedcircuitapplication.domain.model.ID
import com.closedcircuit.closedcircuitapplication.domain.model.Price
import com.closedcircuit.closedcircuitapplication.presentation.util.BaseScreenModel
import com.closedcircuit.closedcircuitapplication.util.InputField
import kotlinx.coroutines.launch

class FundRequestViewModel(
    private val planID: ID,
    private val fundRequestRepository: FundRequestRepository
) : BaseScreenModel<FundRequestUiState, FundRequestResult>() {

    private val loading = mutableStateOf(false)
    private val selectedFundType = mutableStateOf<FundType?>(null)
    private val minimumLoanRange = InputField()
    private val maximumLoanRange = InputField()
    private val numberOfLenders = mutableStateOf<Int?>(null)
    private val graceDuration = mutableStateOf<Int?>(null)
    private val repaymentDuration = mutableStateOf<Int?>(null)
    private val interestRate = InputField()

    @Composable
    override fun uiState(): FundRequestUiState {
        return FundRequestUiState(
            loading = loading.value,
            selectedFundType = selectedFundType.value,
            minimumLoanRange = minimumLoanRange,
            maximumLoanRange = maximumLoanRange,
            numberOfLenders = numberOfLenders.value,
            graceDuration = graceDuration.value,
            repaymentDuration = repaymentDuration.value,
            interestRate = interestRate
        )
    }

    fun onEvent(event: FundRequestUiEvent) {
        when (event) {
            is FundRequestUiEvent.FundTypeChange -> updateFundType(event.fundType)
            is FundRequestUiEvent.GraceDurationChange -> updateGraceDuration(event.duration)
            is FundRequestUiEvent.InterestRateChange -> updateInterestRate(event.interestRate)
            is FundRequestUiEvent.MaxLendersChange -> updateMaxLenders(event.maxLenders)
            is FundRequestUiEvent.MaxRangeChange -> updateMaxLoanRange(event.maxRange)
            is FundRequestUiEvent.MinRangeChange -> updateMinLoanRange(event.minRange)
            is FundRequestUiEvent.RepaymentDurationChange -> updateRepaymentDuration(event.duration)
            FundRequestUiEvent.Submit -> createFundRequest()
        }
    }

    private fun createFundRequest() {
        val request = FundRequest.buildFundRequest(
            meansOfSupport = selectedFundType.value?.name.orEmpty(),
            planId = planID,
            minimumLoanRange = Price(minimumLoanRange.value.toDouble()),
            maximumLoanRange = Price(maximumLoanRange.value.toDouble()),
            maxLenders = numberOfLenders.value,
            graceDuration = graceDuration.value,
            repaymentDuration = repaymentDuration.value,
            interestRate = interestRate.value.toInt()
        )
        screenModelScope.launch {
            loading.value = true
            fundRequestRepository.createFundRequest(request)
                .onSuccess {
                    _resultChannel.send(FundRequestResult.Success)
                }.onError { _, message ->
                    _resultChannel.send(FundRequestResult.Error(message))
                }

            loading.value = false
        }
    }

    private fun updateFundType(type: FundType) {
        selectedFundType.value = type
    }

    private fun updateGraceDuration(duration: Int) {
        graceDuration.value = duration
    }

    private fun updateInterestRate(interestRate: String) {
        this.interestRate.onValueChange(interestRate)
    }

    private fun updateMaxLenders(maxLenders: Int) {
        numberOfLenders.value = maxLenders
    }

    private fun updateMaxLoanRange(maxLoanRange: String) {
        maximumLoanRange.onValueChange(maxLoanRange)
    }

    private fun updateMinLoanRange(minLoanRange: String) {
        minimumLoanRange.onValueChange(minLoanRange)
    }

    private fun updateRepaymentDuration(duration: Int) {
        repaymentDuration.value = duration
    }
}