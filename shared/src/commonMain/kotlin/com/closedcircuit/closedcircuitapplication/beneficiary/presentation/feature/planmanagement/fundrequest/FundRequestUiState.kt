package com.closedcircuit.closedcircuitapplication.beneficiary.presentation.feature.planmanagement.fundrequest

import androidx.compose.runtime.Stable
import com.closedcircuit.closedcircuitapplication.common.domain.model.FundType
import com.closedcircuit.closedcircuitapplication.common.util.InputField

@Stable
data class FundRequestUiState(
    val loading: Boolean,
    val canRequestFunds: Boolean,
    val showLoanSchedule: Boolean,
    val selectedFundType: FundType?,
    val minimumLoanRange: InputField,
    val maximumLoanRange: InputField,
    val numberOfLenders: Int?,
    val graceDuration: Int?,
    val repaymentDuration: Int?,
    val interestRate: InputField
) {
    val canSubmit: Boolean
        get() {
            return when (selectedFundType) {
                FundType.DONATION -> true
                FundType.LOAN,
                FundType.BOTH -> {
                    minimumLoanRange.value.isNotBlank() &&
                            maximumLoanRange.value.isNotBlank() &&
                            interestRate.value.isNotBlank() &&
                            numberOfLenders != null &&
                            graceDuration != null &&
                            repaymentDuration != null
                }

                null -> false
            }
        }
}

sealed interface FundRequestResult {
    data class TokenizeRequestSuccess(val link: String) : FundRequestResult
    data class FundRequestSuccess(val fundType: FundType) : FundRequestResult
    data class Error(val message: String) : FundRequestResult
}
