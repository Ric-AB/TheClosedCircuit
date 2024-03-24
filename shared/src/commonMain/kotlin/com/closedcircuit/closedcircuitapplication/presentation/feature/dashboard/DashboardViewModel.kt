package com.closedcircuit.closedcircuitapplication.presentation.feature.dashboard

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import cafe.adriel.voyager.core.model.screenModelScope
import com.closedcircuit.closedcircuitapplication.core.network.getOrNull
import com.closedcircuit.closedcircuitapplication.domain.donation.DonationRepository
import com.closedcircuit.closedcircuitapplication.domain.plan.PlanRepository
import com.closedcircuit.closedcircuitapplication.domain.user.UserDashboard
import com.closedcircuit.closedcircuitapplication.domain.user.UserRepository
import com.closedcircuit.closedcircuitapplication.presentation.util.BaseScreenModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DashboardViewModel(
    userRepository: UserRepository,
    private val planRepository: PlanRepository,
    private val donationRepository: DonationRepository
) : BaseScreenModel<DashboardUiState, Unit>() {

    private val userDashboardResponseFlow = flow {
        val res = userRepository.getUserDashboard()
        emit(res)
    }

    private val donationsFlow = flow {
        val res = donationRepository.fetchRecentDonations()
        emit(res)
    }.map { it.getOrNull().orEmpty().toImmutableList() }

    private val state = combine(
        userRepository.userFlow,
        planRepository.plansFlow,
        userDashboardResponseFlow,
        donationsFlow,
    ) { user, allPlans, userDashboardResponse, donations ->
        val userDashboard = userDashboardResponse.getOrNull()
        DashboardUiState(
            firstName = user?.firstName?.value,
            recentPlans = allPlans.take(3).toImmutableList(),
            topSponsors = userDashboard?.topSponsors?.toImmutableList(),
            recentDonation = donations,
            planAnalytics = getPlanAnalytics(userDashboard)
        )
    }.stateIn(screenModelScope, SharingStarted.WhileSubscribed(5_000L), DashboardUiState())

    @Composable
    override fun uiState(): DashboardUiState {
        println("FETCHINGGGGG")
        fetchData()
        return state.collectAsState().value
    }

    private fun fetchData() {
        screenModelScope.launch {
            planRepository.fetchPlans()
        }
    }

    private fun getPlanAnalytics(userDashboard: UserDashboard?): PlanAnalyticsUIState? {
        return PlanAnalyticsUIState(
            completedPlansCount = userDashboard?.completedPlansCount ?: return null,
            ongoingPlansCount = userDashboard.ongoingPlansCount,
            notStartedPlansCount = userDashboard.notStartedPlansCount
        )
    }
}