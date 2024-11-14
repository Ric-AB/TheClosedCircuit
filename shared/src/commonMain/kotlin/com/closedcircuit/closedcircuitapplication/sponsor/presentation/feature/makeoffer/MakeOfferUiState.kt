package com.closedcircuit.closedcircuitapplication.sponsor.presentation.feature.makeoffer

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.closedcircuit.closedcircuitapplication.common.domain.chat.ChatUser
import com.closedcircuit.closedcircuitapplication.common.domain.model.Amount
import com.closedcircuit.closedcircuitapplication.common.domain.model.FundType
import com.closedcircuit.closedcircuitapplication.common.domain.model.ID
import com.closedcircuit.closedcircuitapplication.common.domain.util.TypeWithStringProperties
import com.closedcircuit.closedcircuitapplication.common.presentation.util.InputField
import com.closedcircuit.closedcircuitapplication.common.util.orZero
import com.closedcircuit.closedcircuitapplication.sponsor.domain.fundrequest.SponsorFundRequest
import com.closedcircuit.closedcircuitapplication.sponsor.domain.model.FundingLevel
import com.closedcircuit.closedcircuitapplication.sponsor.presentation.component.BudgetItem
import com.closedcircuit.closedcircuitapplication.sponsor.presentation.component.StepItem
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.toImmutableList
import kotlin.math.floor


sealed interface PlanSummaryUiState {
    object Loading : PlanSummaryUiState

    data class Content(
        val isLoggedIn: Boolean,
        val planOwnerId: ID,
        val planOwnerFullName: String,
        val businessSector: String,
        val planDuration: String,
        val estimatedCostPrice: String,
        val estimatedSellingPrice: String,
        val planImage: String,
        val planDescription: String,
        val estimatedProfitPercent: String,
        val estimatedProfitFraction: Float,
        val stepsWithBudgets: ImmutableMap<StepItem, ImmutableList<BudgetItem>>,
        val total: String
    ) : PlanSummaryUiState

    data class Error(val message: String) : PlanSummaryUiState
}

data class FundingLevelUiState(
    val fundingLevel: FundingLevel? = null,
    val fundingLevels: ImmutableList<FundingLevel> = FundingLevel.values().toList()
        .toImmutableList()
) {
    val canProceed get() = fundingLevel != null
}

data class FundingItemsUiState(
    val minLoanAmount: String,
    val maxLoanAmount: String,
    val availableItems: SnapshotStateList<SelectableFundingItem>,
    val enteredAmount: InputField
) {
    val allItemsSelected get() = availableItems.all { it.isSelected }
    val selectedItems get() = availableItems.filter { it.isSelected }
    val totalOfSelectedItems get() = Amount(selectedItems.sumOf { it.cost }, fundRequest.currency)
    val formattedTotalOfSelectedItems get() = totalOfSelectedItems.getFormattedValue()
    val canProceed get() = availableItems.any { it.isSelected }
    val canOfferLoan get() = fundRequest.fundType != FundType.DONATION
    val canOfferDonation get() = fundRequest.fundType != FundType.LOAN
    val isBelowMinimumAmount get() = totalOfSelectedItems.value < fundRequest.minimumLoanRange?.value.orZero()
    val isAboveMaximumAmount get() = totalOfSelectedItems.value > fundRequest.maximumLoanRange?.value.orZero()
    val isEnteredAmountBelowMinAmount get() = enteredAmount.value.toDouble() < fundRequest.minimumLoanRange?.value.orZero()

    private lateinit var fundRequest: SponsorFundRequest

    constructor(fundRequest: SponsorFundRequest) : this(
        minLoanAmount = fundRequest.minimumLoanRange?.getFormattedValue().orEmpty(),
        maxLoanAmount = fundRequest.maximumLoanRange?.getFormattedValue().orEmpty(),
        availableItems = SnapshotStateList(),
        enteredAmount = InputField()
    ) {
        this.fundRequest = fundRequest
    }

    companion object {
        fun init(fundRequest: SponsorFundRequest): FundingItemsUiState {
            return FundingItemsUiState(fundRequest)
        }
    }
}

data class LoanTermsUiState(
    val graceDuration: String,
    val repaymentDuration: String,
    val interestRate: String
) {
    companion object {
        fun init(fundRequest: SponsorFundRequest?): LoanTermsUiState {
            return LoanTermsUiState(
                graceDuration = fundRequest?.graceDuration?.toString().orEmpty(),
                repaymentDuration = fundRequest?.repaymentDuration?.toString().orEmpty(),
                interestRate = fundRequest?.interestRate?.toString().orEmpty()
            )
        }
    }
}

data class LoanScheduleUiState(
    val loanAmount: String,
    val interestAmount: String,
    val repaymentAmount: String,
    val graceDuration: String,
    val repaymentDuration: String,
    val totalDuration: String,
    val repaymentBreakdown: ImmutableList<RepaymentItem>
) {
    companion object {
        fun init(loanAmount: Amount, fundRequest: SponsorFundRequest?): LoanScheduleUiState {
            val graceDuration = fundRequest?.graceDuration.orZero()
            val repaymentDuration = fundRequest?.repaymentDuration.orZero()
            val durationInMonths = graceDuration + repaymentDuration
            val durationInYears = if (durationInMonths.div(12.0) < 1.0) 1
            else floor(durationInMonths.div(12.0)).toInt()

            val interestRate = fundRequest?.interestRate.orZero()
            val interestAmount = calculateInterestAmount(loanAmount, interestRate, durationInYears)
            val repaymentAmount = loanAmount + interestAmount
            val totalDuration = graceDuration + repaymentDuration
            val repaymentAmountPerMonth = calculateMonthlyRepayment(repaymentAmount, totalDuration)
            val repaymentBreakdown = List(totalDuration - 1) {
                RepaymentItem("Month ${it + 1}", repaymentAmountPerMonth.getFormattedValue())
            }.toImmutableList()

            return LoanScheduleUiState(
                loanAmount = loanAmount.getFormattedValue(),
                interestAmount = interestAmount.getFormattedValue(),
                repaymentAmount = repaymentAmount.getFormattedValue(),
                graceDuration = graceDuration.toString(),
                repaymentDuration = repaymentDuration.toString(),
                totalDuration = totalDuration.toString(),
                repaymentBreakdown = repaymentBreakdown
            )
        }

        private fun calculateInterestAmount(
            amount: Amount,
            interestRate: Int,
            duration: Int
        ): Amount {
            return Amount(
                value = (amount.value * interestRate * duration).div(100),
                currency = amount.currency
            )
        }

        private fun calculateMonthlyRepayment(repaymentAmount: Amount, totalDuration: Int): Amount {
            return Amount(
                value = repaymentAmount.value / totalDuration,
                currency = repaymentAmount.currency
            )
        }
    }
}

data class SelectableFundingItem(
    val id: String,
    val name: String,
    val formattedCost: String,
    val cost: Double,
    val isSelected: Boolean
) : TypeWithStringProperties {
    override val properties: List<String>
        get() = listOf(name, formattedCost)
}

data class RepaymentItem(
    val index: String,
    val value: String
) : TypeWithStringProperties {
    override val properties: List<String>
        get() = listOf(index, value)
}

sealed interface MakeOfferResult {
    object LoanOfferSuccess : MakeOfferResult

    data class DonationOfferSuccess(val paymentLink: String) : MakeOfferResult

    data class ChatUserSuccess(val chatUser: ChatUser) : MakeOfferResult

    data class Error(val message: String) : MakeOfferResult
}