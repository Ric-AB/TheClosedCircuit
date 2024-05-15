package com.closedcircuit.closedcircuitapplication.sponsor.presentation.feature.makeoffer

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.model.ID
import com.closedcircuit.closedcircuitapplication.core.network.onError
import com.closedcircuit.closedcircuitapplication.core.network.onSuccess
import com.closedcircuit.closedcircuitapplication.sponsor.domain.plan.PlanRepository
import kotlinx.coroutines.launch

class MakeOfferViewModel(
    private val planRepository: PlanRepository
) : ScreenModel {

    var planSummaryState by mutableStateOf<PlanSummaryUiState>(PlanSummaryUiState.Loading)
        private set

    init {
        fetchPlanByFundRequestId()
    }

    fun onEvent(event: MakeOfferEvent) {
        when (event) {
            MakeOfferEvent.FetchPlan -> fetchPlanByFundRequestId()
            is MakeOfferEvent.FundingLevelChange -> {}
        }
    }

    private fun fetchPlanByFundRequestId() {
        screenModelScope.launch {
            planRepository.fetchPlanByFundRequestId(ID("5cbdf235-51e7-45d4-b68e-4e408050cd2c"))
                .onSuccess {
                    val estimatedCostPrice = it.estimatedCostPrice.value
                    val estimatedSellingPrice = it.estimatedSellingPrice.value
                    val estimatedProfitFraction =
                        (estimatedSellingPrice - estimatedCostPrice) / estimatedCostPrice
                    val estimatedProfitPercent = estimatedProfitFraction.times(100)

                    planSummaryState = PlanSummaryUiState.Content(
                        businessSector = it.sector,
                        planDuration = it.duration.value.toString(),
                        estimatedCostPrice = estimatedCostPrice.toString(),
                        estimatedSellingPrice = estimatedSellingPrice.toString(),
                        planImage = it.avatar.value,
                        planDescription = it.description,
                        estimatedProfitPercent = estimatedProfitPercent.toString(),
                        estimatedProfitFraction = estimatedProfitFraction.toFloat()
                    )
                }.onError { _, message ->
                    println("#######:: $message")
                    planSummaryState = PlanSummaryUiState.Error(message)
                }
        }
    }
}