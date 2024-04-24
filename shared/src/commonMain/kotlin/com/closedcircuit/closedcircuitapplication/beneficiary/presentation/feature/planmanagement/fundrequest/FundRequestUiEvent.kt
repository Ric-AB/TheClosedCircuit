package com.closedcircuit.closedcircuitapplication.beneficiary.presentation.feature.planmanagement.fundrequest

import com.closedcircuit.closedcircuitapplication.beneficiary.domain.model.FundType

sealed interface FundRequestUiEvent {
    object TokenizeCard: FundRequestUiEvent
    object SubmitFundRequest : FundRequestUiEvent
    data class FundTypeChange(val fundType: FundType) : FundRequestUiEvent
    data class MinRangeChange(val minRange: String) : FundRequestUiEvent
    data class MaxRangeChange(val maxRange: String) : FundRequestUiEvent
    data class GraceDurationChange(val duration: Int) : FundRequestUiEvent
    data class RepaymentDurationChange(val duration: Int) : FundRequestUiEvent
    data class MaxLendersChange(val maxLenders: Int) : FundRequestUiEvent
    data class InterestRateChange(val interestRate: String) : FundRequestUiEvent
}