package com.closedcircuit.closedcircuitapplication.sponsor.presentation.feature.dashboard

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.user.UserRepository
import com.closedcircuit.closedcircuitapplication.core.network.onError
import com.closedcircuit.closedcircuitapplication.core.network.onSuccess
import com.closedcircuit.closedcircuitapplication.sponsor.domain.plan.PlanRepository
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch

class SponsorDashboardViewModel(
    private val userRepository: UserRepository,
    private val planRepository: PlanRepository
) : ScreenModel {
    var state by mutableStateOf<SponsorDashboardUiState>(SponsorDashboardUiState.Loading); private set

    init {
        fetchDashboardPlans()
    }

    private fun fetchDashboardPlans() {
        screenModelScope.launch {
            val userFirstName = userRepository.userFlow.value?.firstName?.value.orEmpty()
            planRepository.fetchSponsoredPlans().onSuccess {
                val plans = it.toImmutableList()
                state = SponsorDashboardUiState.Content(
                    userFirstName = userFirstName,
                    plans = plans
                )
            }.onError { _, message ->
                state = SponsorDashboardUiState.Error(message)
            }
        }
    }
}