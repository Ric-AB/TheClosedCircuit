package com.closedcircuit.closedcircuitapplication.sponsor.presentation.feature.fundedplan.details

import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.closedcircuit.closedcircuitapplication.common.domain.model.ID
import com.closedcircuit.closedcircuitapplication.core.network.onError
import com.closedcircuit.closedcircuitapplication.core.network.onSuccess
import com.closedcircuit.closedcircuitapplication.sponsor.domain.plan.PlanRepository
import kotlinx.coroutines.launch

class FundedPlanDetailsViewModel(
    private val planID: ID,
    private val planRepository: PlanRepository
) : ScreenModel {

    val state = mutableStateOf<FundedPlanDetailsUiState>(FundedPlanDetailsUiState.Loading)

    init {
        fetchFundedPlanDetails()
    }

    private fun fetchFundedPlanDetails() {
        screenModelScope.launch {
            planRepository.fetchFundedPlanDetails(planID).onSuccess {
                state.value = FundedPlanDetailsUiState.Content(
                    planImageUrl = it.avatar.value,
                    planDescription = it.description,
                    planSector = it.sector,
                    planDuration = it.duration.value,
                    estimatedCostPrice = it.estimatedCostPrice.getFormattedValue(),
                    estimatedSellingPrice = it.estimatedSellingPrice.getFormattedValue()
                )
            }.onError { _, message ->
                state.value = FundedPlanDetailsUiState.Error(message)
            }
        }
    }

}