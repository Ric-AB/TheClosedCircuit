package com.closedcircuit.closedcircuitapplication.presentation.feature.planmanagement.fundrequest

import androidx.compose.runtime.Stable
import com.closedcircuit.closedcircuitapplication.domain.model.FundType
import com.closedcircuit.closedcircuitapplication.util.InputField

@Stable
data class FundRequestUiState(
    val loading: Boolean,
    val selectedFundType: FundType?,
    val minimumLoanRange: InputField,
    val maximumLoanRange: InputField,
    val numberOfLenders: Int?,
    val graceDuration: Int?,
    val repaymentDuration: Int?,
    val interestRate: InputField
)

sealed interface FundRequestResult {
    object Success : FundRequestResult
    data class Error(val message: String) : FundRequestResult
}
