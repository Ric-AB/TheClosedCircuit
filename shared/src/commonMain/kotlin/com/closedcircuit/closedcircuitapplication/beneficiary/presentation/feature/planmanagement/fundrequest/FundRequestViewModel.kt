package com.closedcircuit.closedcircuitapplication.beneficiary.presentation.feature.planmanagement.fundrequest

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.screenModelScope
import com.closedcircuit.closedcircuitapplication.core.network.onError
import com.closedcircuit.closedcircuitapplication.core.network.onSuccess
import com.closedcircuit.closedcircuitapplication.common.domain.fundrequest.FundRequest
import com.closedcircuit.closedcircuitapplication.common.domain.fundrequest.FundRequestRepository
import com.closedcircuit.closedcircuitapplication.common.domain.model.FundType
import com.closedcircuit.closedcircuitapplication.common.domain.model.Amount
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.payment.PaymentRepository
import com.closedcircuit.closedcircuitapplication.common.domain.plan.Plan
import com.closedcircuit.closedcircuitapplication.common.domain.user.UserRepository
import com.closedcircuit.closedcircuitapplication.common.presentation.util.BaseScreenModel
import com.closedcircuit.closedcircuitapplication.common.presentation.util.InputField
import com.closedcircuit.closedcircuitapplication.common.util.orFalse
import kotlinx.coroutines.launch

class FundRequestViewModel(
    private val plan: Plan,
    private val fundRequestRepository: FundRequestRepository,
    private val paymentRepository: PaymentRepository,
    userRepository: UserRepository
) : BaseScreenModel<FundRequestUiState, FundRequestResult>() {

    private val canRequestFund =
        mutableStateOf(userRepository.userFlow.value?.isCardTokenized.orFalse())

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
            showLoanSchedule = selectedFundType.value != FundType.DONATION,
            canRequestFunds = canRequestFund.value,
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
            FundRequestUiEvent.SubmitFundRequest -> createFundRequest()
            FundRequestUiEvent.TokenizeCard -> generateTokenizationLink()
        }
    }

    private fun createFundRequest() {
        val request = buildFundRequest()
        screenModelScope.launch {
            loading.value = true
            fundRequestRepository.createFundRequest(request)
                .onSuccess {
                    val fundType = selectedFundType.value!!
                    _resultChannel.send(FundRequestResult.FundRequestSuccess(fundType))
                }.onError { _, message ->
                    _resultChannel.send(FundRequestResult.Error(message))
                }

            loading.value = false
        }
    }

    private fun generateTokenizationLink() {
        screenModelScope.launch {
            loading.value = true
            paymentRepository.generateTokenizationLink()
                .onSuccess { paymentLink ->
                    _resultChannel.send(FundRequestResult.TokenizeRequestSuccess(paymentLink))
                }.onError { _, message ->
                    _resultChannel.send(FundRequestResult.Error(message))
                }

            loading.value = false
        }
    }

    private fun buildFundRequest(): FundRequest {
        val fundType = selectedFundType.value
        var fundRequest = FundRequest.buildFundRequest(
            fundType = fundType!!,
            planId = plan.id,
        )

        if (fundType != FundType.DONATION) {
            fundRequest = fundRequest.copy(
                minimumLoanRange = Amount(minimumLoanRange.value.toDouble()),
                maximumLoanRange = Amount(maximumLoanRange.value.toDouble()),
                maxLenders = numberOfLenders.value,
                graceDuration = graceDuration.value,
                repaymentDuration = repaymentDuration.value,
                interestRate = interestRate.value.toInt()
            )
        }
        return fundRequest
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