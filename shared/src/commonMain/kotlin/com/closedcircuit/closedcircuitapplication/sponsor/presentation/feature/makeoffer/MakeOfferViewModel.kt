package com.closedcircuit.closedcircuitapplication.sponsor.presentation.feature.makeoffer

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.budget.Budget
import com.closedcircuit.closedcircuitapplication.common.domain.model.ID
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.step.Step
import com.closedcircuit.closedcircuitapplication.common.util.replaceAll
import com.closedcircuit.closedcircuitapplication.common.util.round
import com.closedcircuit.closedcircuitapplication.core.network.onError
import com.closedcircuit.closedcircuitapplication.core.network.onSuccess
import com.closedcircuit.closedcircuitapplication.sponsor.domain.plan.PlanRepository
import com.closedcircuit.closedcircuitapplication.sponsor.domain.plan.SponsorPlan
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.coroutines.launch

class MakeOfferViewModel(
    private val planRepository: PlanRepository
) : ScreenModel {

    private lateinit var sponsoringPlan: SponsorPlan
    var planSummaryState by mutableStateOf<PlanSummaryUiState>(PlanSummaryUiState.Loading)
        private set

    var fundingLevelState by mutableStateOf(FundingLevelUiState())
        private set

    val fundingItemsState by mutableStateOf(FundingItemsUiState())

    lateinit var loanTermsState: MutableState<LoanTermsUiState>
    lateinit var loanScheduleState: MutableState<LoanScheduleUiState>

    init {
        fetchPlanByFundRequestId()
    }

    fun onEvent(event: MakeOfferEvent) {
        when (event) {
            MakeOfferEvent.FetchPlan -> fetchPlanByFundRequestId()
            is MakeOfferEvent.FundingLevelChange -> updateFundingLevel(event.fundingLevel)
            MakeOfferEvent.ToggleAllFundingItems -> toggleAllFundingItems()
            is MakeOfferEvent.ToggleFundingItem -> toggleFundingItem(event.index)
            MakeOfferEvent.SubmitSelection -> createLoanSchedule()
        }
    }

    private fun fetchPlanByFundRequestId() {
        screenModelScope.launch {
            planRepository.fetchPlanByFundRequestId(ID("5cbdf235-51e7-45d4-b68e-4e408050cd2c"))
                .onSuccess { sponsorPlan ->
                    initialize(sponsorPlan)

                    val estimatedCostPrice = sponsorPlan.estimatedCostPrice.value
                    val estimatedSellingPrice = sponsorPlan.estimatedSellingPrice.value
                    val estimatedProfitFraction = estimatedSellingPrice.minus(estimatedCostPrice)
                        .div(estimatedCostPrice)
                        .round(2)

                    val estimatedProfitPercent = estimatedProfitFraction.times(100)
                    val stepsWithBudget = sponsorPlan.steps
                        .associateWith { step ->
                            sponsorPlan.budgets.filter { it.stepID == step.id }.toImmutableList()
                        }.toImmutableMap()

                    planSummaryState = PlanSummaryUiState.Content(
                        businessSector = sponsorPlan.sector,
                        planDuration = sponsorPlan.duration.value.toString(),
                        estimatedCostPrice = estimatedCostPrice.toString(),
                        estimatedSellingPrice = estimatedSellingPrice.toString(),
                        planImage = sponsorPlan.avatar.value,
                        planDescription = sponsorPlan.description,
                        estimatedProfitPercent = estimatedProfitPercent.toString(),
                        estimatedProfitFraction = estimatedProfitFraction.toFloat(),
                        stepsWithBudgets = stepsWithBudget,
                        total = sponsorPlan.targetAmount.value.toString()
                    )
                }.onError { _, message ->
                    planSummaryState = PlanSummaryUiState.Error(message)
                }
        }
    }

    private fun initialize(sponsorPlan: SponsorPlan) {
        sponsoringPlan = sponsorPlan
        loanTermsState = mutableStateOf(LoanTermsUiState.init(sponsorPlan.fundRequest))
    }

    private fun createLoanSchedule() {
        loanScheduleState = mutableStateOf(
            LoanScheduleUiState.init(
                loanAmount = fundingItemsState.formattedTotalOfSelectedItems,
                fundRequest = sponsoringPlan.fundRequest
            )
        )
    }

    private fun updateFundingLevel(fundingLevel: FundingLevel) {
        fundingLevelState = fundingLevelState.copy(fundingLevel = fundingLevel)
        updateSelectableItems(fundingLevel)
    }

    private fun updateSelectableItems(fundingLevel: FundingLevel) {
        val availableItems = fundingItemsState.availableItems
        when (fundingLevel) {
            FundingLevel.PLAN -> availableItems.add(sponsoringPlan.toFundingItem())
            FundingLevel.STEP -> availableItems.addAll(sponsoringPlan.steps.map { it.toFundingItem() })
            FundingLevel.BUDGET -> availableItems.addAll(sponsoringPlan.budgets.map { it.toFundingItem() })
            FundingLevel.OTHER -> {}
        }
    }

    private fun toggleAllFundingItems() {
        val newSelectionState = !fundingItemsState.allItemsSelected
        val newList = fundingItemsState.availableItems
            .map { it.copy(isSelected = newSelectionState) }

        fundingItemsState.availableItems.replaceAll(newList)
    }

    private fun toggleFundingItem(index: Int) {
        val currentItem = fundingItemsState.availableItems[index]
        val currentItemSelected = currentItem.isSelected
        fundingItemsState.availableItems[index] =
            currentItem.copy(isSelected = !currentItemSelected)
    }

    private fun SponsorPlan.toFundingItem(): FundingItem {
        return FundingItem(
            id = id,
            name = name,
            formattedCost = targetAmount.value.toString(),
            cost = targetAmount.value,
            isSelected = false
        )
    }

    private fun Step.toFundingItem(): FundingItem {
        return FundingItem(
            id = id,
            name = name,
            formattedCost = targetFunds.value.toString(),
            cost = targetFunds.value,
            isSelected = false
        )
    }

    private fun Budget.toFundingItem(): FundingItem {
        return FundingItem(
            id = id,
            name = name,
            formattedCost = cost.value.toString(),
            cost = cost.value,
            isSelected = false
        )
    }
}