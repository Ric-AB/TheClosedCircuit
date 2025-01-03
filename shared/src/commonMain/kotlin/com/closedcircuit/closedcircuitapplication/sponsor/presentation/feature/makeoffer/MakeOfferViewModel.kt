package com.closedcircuit.closedcircuitapplication.sponsor.presentation.feature.makeoffer

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.payment.PaymentRepository
import com.closedcircuit.closedcircuitapplication.common.domain.app.AppSettingsRepository
import com.closedcircuit.closedcircuitapplication.common.domain.budget.Budget
import com.closedcircuit.closedcircuitapplication.common.domain.model.Amount
import com.closedcircuit.closedcircuitapplication.common.domain.model.AuthenticationState
import com.closedcircuit.closedcircuitapplication.common.domain.model.FundType
import com.closedcircuit.closedcircuitapplication.common.domain.model.ID
import com.closedcircuit.closedcircuitapplication.common.domain.model.ProfileType
import com.closedcircuit.closedcircuitapplication.common.domain.step.Step
import com.closedcircuit.closedcircuitapplication.common.domain.usecase.IsLoggedInUseCase
import com.closedcircuit.closedcircuitapplication.common.domain.user.UserRepository
import com.closedcircuit.closedcircuitapplication.common.util.capitalizeFirstChar
import com.closedcircuit.closedcircuitapplication.common.util.replaceAll
import com.closedcircuit.closedcircuitapplication.common.util.round
import com.closedcircuit.closedcircuitapplication.core.network.onComplete
import com.closedcircuit.closedcircuitapplication.core.network.onError
import com.closedcircuit.closedcircuitapplication.core.network.onSuccess
import com.closedcircuit.closedcircuitapplication.sponsor.data.offer.dto.OfferPayload
import com.closedcircuit.closedcircuitapplication.sponsor.domain.model.FundingLevel
import com.closedcircuit.closedcircuitapplication.sponsor.domain.offer.OfferRepository
import com.closedcircuit.closedcircuitapplication.sponsor.domain.plan.PlanRepository
import com.closedcircuit.closedcircuitapplication.sponsor.domain.plan.SponsorPlan
import com.closedcircuit.closedcircuitapplication.sponsor.presentation.component.FundingItem
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.launch

class MakeOfferViewModel(
    private val planID: ID,
    private val planRepository: PlanRepository,
    private val offerRepository: OfferRepository,
    private val settingsRepository: AppSettingsRepository,
    private val paymentRepository: PaymentRepository,
    private val userRepository: UserRepository,
    private val isLoggedInUseCase: IsLoggedInUseCase
) : ScreenModel {

    private lateinit var sponsoringPlan: SponsorPlan
    var planSummaryState by mutableStateOf<PlanSummaryUiState>(PlanSummaryUiState.Loading); private set
    var fundingLevelState by mutableStateOf(FundingLevelUiState()); private set
    lateinit var fundingItemsState: MutableState<FundingItemsUiState>
    lateinit var loanTermsState: MutableState<LoanTermsUiState>
    lateinit var loanScheduleState: MutableState<LoanScheduleUiState>
    lateinit var fundType: FundType; private set
    val loading = mutableStateOf(false)
    val loadingUser = mutableStateOf(false)

    private val _makeOfferResultChannel = Channel<MakeOfferResult>()
    val makeOfferResultChannel: ReceiveChannel<MakeOfferResult> = _makeOfferResultChannel

    init {
        fetchPlanByFundRequestId()
    }

    fun onEvent(event: MakeOfferEvent) {
        when (event) {
            is MakeOfferEvent.FundingLevelChange -> updateFundingLevel(event.fundingLevel)
            is MakeOfferEvent.FundTypeChange -> updateFundType(event.fundType)
            is MakeOfferEvent.ToggleFundingItem -> toggleFundingItem(event.index)
            is MakeOfferEvent.EnterAmount -> updateEnteredAmount(event.amount)
            MakeOfferEvent.ToggleAllFundingItems -> toggleAllFundingItems()
            MakeOfferEvent.CreateSchedule -> createLoanSchedule()
            MakeOfferEvent.FetchChatUser -> fetchChatUser()
            MakeOfferEvent.SubmitOffer -> submitOffer()
        }
    }

    private fun fetchPlanByFundRequestId() {
        screenModelScope.launch {
            planRepository.fetchPlanByFundRequestId(planID)
                .onSuccess { sponsorPlan ->
                    initialize(sponsorPlan)

                    val isLoggedIn = isLoggedInUseCase() == AuthenticationState.LOGGED_IN
                    val estimatedCostPrice = sponsorPlan.estimatedCostPrice
                    val estimatedSellingPrice = sponsorPlan.estimatedSellingPrice
                    val estimatedProfitFraction = estimatedSellingPrice.minus(estimatedCostPrice)
                        .div(estimatedCostPrice)
                        .value
                        .round()

                    val estimatedProfitPercent = estimatedProfitFraction.times(100)
                    val stepsWithBudget = sponsorPlan.steps
                        .map { step -> step.toFundingItem() }
                        .associateWith { step ->
                            sponsorPlan.budgets
                                .filter { it.stepID.value == step.id }
                                .map { budget -> budget.toFundingItem() }
                                .toImmutableList()
                        }.toImmutableMap()

                    planSummaryState = PlanSummaryUiState.Content(
                        isLoggedIn = isLoggedIn,
                        planOwnerId = sponsorPlan.beneficiaryId!!,
                        planOwnerFullName = sponsorPlan.beneficiaryFullName.value,
                        businessSector = sponsorPlan.sector.capitalizeFirstChar(),
                        planDuration = sponsorPlan.duration.value.toString(),
                        estimatedCostPrice = estimatedCostPrice.getFormattedValue(),
                        estimatedSellingPrice = estimatedSellingPrice.getFormattedValue(),
                        planImage = sponsorPlan.avatar.value,
                        planDescription = sponsorPlan.description,
                        estimatedProfitPercent = estimatedProfitPercent.toString(),
                        estimatedProfitFraction = estimatedProfitFraction.toFloat(),
                        stepsWithBudgets = stepsWithBudget,
                        total = sponsorPlan.targetAmount.getFormattedValue()
                    )
                }.onError { _, message ->
                    planSummaryState = PlanSummaryUiState.Error(message)
                }
        }
    }

    private fun submitOffer() {
        val payload = getOfferPayload()
        loading.value = true
        screenModelScope.launch {
            offerRepository.sendOffer(payload)
                .onSuccess { response ->
                    if (fundType == FundType.LOAN) handleLoanSuccess()
                    else generatePaymentLink(response.id)
                }.onError { _, message ->
                    handleMakeOfferError(message)
                }
        }
    }

    private suspend fun generatePaymentLink(loanID: String) {
        paymentRepository.generatePaymentLink(
            loanID = ID(loanID),
            amount = Amount(getTotalAmount())
        ).onSuccess { paymentLink ->
            handlePaymentLinkSuccess(paymentLink)
        }.onError { _, message ->
            handleMakeOfferError(message)
        }
    }

    private suspend fun handleLoanSuccess() {
        loading.value = false
        updateActiveProfile()
        _makeOfferResultChannel.send(MakeOfferResult.LoanOfferSuccess)
    }

    private suspend fun handlePaymentLinkSuccess(paymentLink: String) {
        loading.value = false
        updateActiveProfile()
        _makeOfferResultChannel.send(MakeOfferResult.DonationOfferSuccess(paymentLink))
    }

    private suspend fun handleMakeOfferError(errorMessage: String) {
        loading.value = false
        _makeOfferResultChannel.send(MakeOfferResult.Error(errorMessage))
    }

    private fun getTotalAmount(): Double {
        return if (fundingLevelState.fundingLevel == FundingLevel.OTHER) {
            fundingItemsState.value.enteredAmount.value.toDouble()
        } else {
            fundingItemsState.value.selectedItems.sumOf { it.cost }
        }
    }

    private fun getOfferPayload(): OfferPayload {
        val fundingLevel = fundingLevelState.fundingLevel!!
        val selectedItemIds = fundingItemsState.value.selectedItems.map { it.id }
        val otherAmount =
            if (fundingLevelState.fundingLevel == FundingLevel.OTHER) fundingItemsState.value.enteredAmount
            else null

        val fundingLevelValue = fundingLevel.requestValue
        val fundRequestId = sponsoringPlan.fundRequest.id.value
        val isDonation = fundType == FundType.DONATION
        val stepIds = if (fundingLevel == FundingLevel.STEP) selectedItemIds else null
        val budgetIds = if (fundingLevel == FundingLevel.BUDGET) selectedItemIds else null

        return OfferPayload(
            otherAmount = otherAmount?.value,
            fundingLevel = fundingLevelValue,
            fundRequest = fundRequestId,
            isDonation = isDonation,
            steps = stepIds,
            budgets = budgetIds
        )
    }

    private fun initialize(sponsorPlan: SponsorPlan) {
        sponsoringPlan = sponsorPlan
        loanTermsState = mutableStateOf(LoanTermsUiState.init(sponsorPlan.fundRequest))
        fundingItemsState = mutableStateOf(FundingItemsUiState.init(sponsoringPlan.fundRequest))
    }

    private fun createLoanSchedule() {
        val currency = sponsoringPlan.currency
        val loanAmount = if (fundingLevelState.fundingLevel == FundingLevel.OTHER)
            Amount(fundingItemsState.value.enteredAmount.value.toDouble(), currency)
        else {
            fundingItemsState.value.totalOfSelectedItems.copy(currency = currency)
        }

        loanScheduleState = mutableStateOf(
            LoanScheduleUiState.init(
                loanAmount = loanAmount,
                fundRequest = sponsoringPlan.fundRequest
            )
        )
    }

    private fun fetchChatUser() {
        val state = planSummaryState
        if (state is PlanSummaryUiState.Content) {
            loadingUser.value = true
            screenModelScope.launch {
                userRepository.fetchChatUser(state.planOwnerId).onComplete {
                    loadingUser.value = false
                }.onSuccess {
                    _makeOfferResultChannel.send(MakeOfferResult.ChatUserSuccess(it))
                }.onError { _, message ->
                    _makeOfferResultChannel.send(MakeOfferResult.Error(message))
                }
            }
        }
    }

    private fun updateEnteredAmount(amount: String) {
        fundingItemsState.value.enteredAmount.onValueChange(amount)
    }

    private fun updateFundingLevel(fundingLevel: FundingLevel) {
        fundingLevelState = fundingLevelState.copy(fundingLevel = fundingLevel)
        updateFundingItems(fundingLevel)
    }

    private fun updateFundType(fundType: FundType) {
        this.fundType = fundType
    }

    private fun updateFundingItems(fundingLevel: FundingLevel) {
        val availableItems = fundingItemsState.value.availableItems
        when (fundingLevel) {
            FundingLevel.PLAN -> availableItems.replaceAll(listOf(sponsoringPlan.toSelectableItem()))
            FundingLevel.STEP -> availableItems.replaceAll(sponsoringPlan.steps.map { it.toSelectableItem() })
            FundingLevel.BUDGET -> availableItems.replaceAll(sponsoringPlan.budgets.map { it.toSelectableItem() })
            FundingLevel.OTHER -> {}
        }
    }

    private fun toggleAllFundingItems() {
        val newSelectionState = !fundingItemsState.value.allItemsSelected
        val newList = fundingItemsState.value.availableItems
            .map { it.copy(isSelected = newSelectionState) }

        fundingItemsState.value.availableItems.replaceAll(newList)
    }

    private fun toggleFundingItem(itemIndex: Int) {
        val availableItems = fundingItemsState.value.availableItems
        val currentItem = availableItems[itemIndex]
        val newSelectionState = !currentItem.isSelected
        val deselectSubsequentItems = !newSelectionState
        val currentItemSelected = currentItem.isSelected
        availableItems[itemIndex] = currentItem.copy(isSelected = !currentItemSelected)

        val updatedList = availableItems.mapIndexed { index, selectableFundingItem ->
            when {
                index == itemIndex -> selectableFundingItem.copy(isSelected = newSelectionState)
                deselectSubsequentItems && index > itemIndex ->
                    selectableFundingItem.copy(isSelected = false)

                else -> selectableFundingItem
            }
        }

        availableItems.replaceAll(updatedList)
    }

    private suspend fun updateActiveProfile() {
        settingsRepository.setActiveProfile(ProfileType.SPONSOR)
    }

    private fun SponsorPlan.toSelectableItem(): SelectableFundingItem {
        return SelectableFundingItem(
            id = id.value,
            name = name,
            formattedCost = targetAmount.getFormattedValue(),
            cost = targetAmount.value,
            isSelected = false
        )
    }

    private fun Step.toSelectableItem(): SelectableFundingItem {
        return SelectableFundingItem(
            id = id.value,
            name = name,
            formattedCost = targetFunds.getFormattedValue(),
            cost = targetFunds.value,
            isSelected = false
        )
    }

    private fun Budget.toSelectableItem(): SelectableFundingItem {
        return SelectableFundingItem(
            id = id.value,
            name = name,
            formattedCost = cost.getFormattedValue(),
            cost = cost.value,
            isSelected = false
        )
    }

    private fun Step.toFundingItem(): FundingItem {
        return FundingItem(
            id = id.value,
            name = name,
            formattedCost = targetFunds.getFormattedValue(),
        )
    }

    private fun Budget.toFundingItem(): FundingItem {
        return FundingItem(
            id = id.value,
            name = name,
            formattedCost = cost.getFormattedValue(),
        )
    }
}