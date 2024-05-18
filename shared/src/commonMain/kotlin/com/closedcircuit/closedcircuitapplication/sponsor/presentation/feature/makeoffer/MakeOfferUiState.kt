package com.closedcircuit.closedcircuitapplication.sponsor.presentation.feature.makeoffer

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.closedcircuit.closedcircuitapplication.common.domain.model.Amount
import com.closedcircuit.closedcircuitapplication.common.domain.model.ID
import com.closedcircuit.closedcircuitapplication.common.domain.fundrequest.FundRequest
import com.closedcircuit.closedcircuitapplication.common.domain.util.TypeWithStringProperties
import com.closedcircuit.closedcircuitapplication.common.util.orZero
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.toImmutableList
import kotlin.math.floor

typealias Step = FundingItem
typealias Budget = FundingItem

sealed interface PlanSummaryUiState {
    object Loading : PlanSummaryUiState

    data class Content(
        val businessSector: String,
        val planDuration: String,
        val estimatedCostPrice: String,
        val estimatedSellingPrice: String,
        val planImage: String,
        val planDescription: String,
        val estimatedProfitPercent: String,
        val estimatedProfitFraction: Float,
        val stepsWithBudgets: ImmutableMap<Step, ImmutableList<Budget>>,
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
    val availableItems: SnapshotStateList<FundingItem> = SnapshotStateList()
) {
    val allItemsSelected get() = availableItems.all { it.isSelected }
    val selectedItems get() = availableItems.filter { it.isSelected }
    val totalOfSelectedItems get() = Amount(selectedItems.sumOf { it.cost })
    val formattedTotalOfSelectedItems get() = totalOfSelectedItems.getFormattedValue()
    val canProceed get() = availableItems.any { it.isSelected }
}

data class LoanTermsUiState(
    val graceDuration: String,
    val repaymentDuration: String,
    val interestRate: String
) {
    companion object {
        fun init(fundRequest: FundRequest?): LoanTermsUiState {
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
    val repaymentBreakdown: ImmutableList<TableItem>
) {
    companion object {
        fun init(loanAmount: Double, fundRequest: FundRequest?): LoanScheduleUiState {
            val graceDuration = fundRequest?.graceDuration.orZero()
            val repaymentDuration = fundRequest?.repaymentDuration.orZero()
            val durationInMonths = graceDuration + repaymentDuration
            val durationInYears = if (durationInMonths.div(12.0) < 1.0) 1
            else floor(durationInMonths.div(12.0)).toInt()

            val interestRate = fundRequest?.interestRate.orZero()
            val interestAmount = (loanAmount * interestRate * durationInYears).div(100)
            val repaymentAmount = (loanAmount + interestAmount)
            val totalDuration = graceDuration + repaymentDuration
            val repaymentAmountPerMonth = Amount(repaymentAmount / totalDuration)
            val repaymentBreakdown = List(totalDuration - 1) {
                TableItem("Month ${it + 1}", repaymentAmountPerMonth.getFormattedValue())
            }.toImmutableList()

            return LoanScheduleUiState(
                loanAmount = loanAmount.toString(),
                interestAmount = interestAmount.toString(),
                repaymentAmount = repaymentAmount.toString(),
                graceDuration = graceDuration.toString(),
                repaymentDuration = repaymentDuration.toString(),
                totalDuration = totalDuration.toString(),
                repaymentBreakdown = repaymentBreakdown
            )
        }
    }
}

data class TableItem(
    val index: String,
    val value: String
) : TypeWithStringProperties {
    override val properties: List<String>
        get() = listOf(index, value)
}

data class FundingItem(
    val id: ID,
    val name: String,
    val formattedCost: String,
    val cost: Double,
    val isSelected: Boolean
) : TypeWithStringProperties {
    override val properties: List<String>
        get() = listOf(name, formattedCost)
}