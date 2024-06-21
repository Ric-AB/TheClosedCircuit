package com.closedcircuit.closedcircuitapplication.sponsor.presentation.feature.fundedplan.list

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.closedcircuit.closedcircuitapplication.core.network.onSuccess
import com.closedcircuit.closedcircuitapplication.sponsor.domain.plan.PlanRepository
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch

class FundedPlanListViewModel(
    private val planRepository: PlanRepository
) : ScreenModel {

    val state: MutableState<FundedPlanListUiState> = mutableStateOf(FundedPlanListUiState.Loading)

    init {
        fetchFundedPlans()
    }

    private fun fetchFundedPlans() {
        screenModelScope.launch {
            planRepository.fetchFundedPlans().onSuccess {
                state.value = FundedPlanListUiState.Content(it.toImmutableList())
            }
        }
    }
}