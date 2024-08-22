package com.closedcircuit.closedcircuitapplication.beneficiary.presentation.feature.dashboard

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import cafe.adriel.voyager.core.model.screenModelScope
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.donation.DonationRepository
import com.closedcircuit.closedcircuitapplication.common.domain.plan.PlanRepository
import com.closedcircuit.closedcircuitapplication.common.domain.user.UserRepository
import com.closedcircuit.closedcircuitapplication.common.presentation.util.BaseScreenModel
import com.closedcircuit.closedcircuitapplication.common.util.Zero
import com.closedcircuit.closedcircuitapplication.common.util.orZero
import com.closedcircuit.closedcircuitapplication.core.network.getOrNull
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val userRepository: UserRepository,
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
        planRepository.getRecentPlans(),
        userDashboardResponseFlow,
        donationsFlow,
    ) { user, recentPlans, userDashboardResponse, donations ->
        val userDashboard = userDashboardResponse.getOrNull()
        val completedPlanCount = userDashboard?.completedPlansCount.orZero()
        val ongoingPlanCount = userDashboard?.ongoingPlansCount.orZero()
        val notStartedPlanCount = userDashboard?.notStartedPlansCount.orZero()
        val showAnalytics = listOf(completedPlanCount, ongoingPlanCount, notStartedPlanCount)
            .any { it > Int.Zero }

        if (user == null) {
            return@combine DashboardUiState.Error("User does not exist.")
        }

        if (recentPlans.isEmpty()) {
            return@combine DashboardUiState.Empty(user.isVerified, user.email, user.firstName.value)
        }

        DashboardUiState.Content(
            firstName = user.firstName.value,
            walletBalance = user.walletBalance?.getFormattedValue().orEmpty(),
            recentPlans = recentPlans,
            topSponsors = userDashboard?.topSponsors?.toImmutableList(),
            recentDonation = donations,
            completedPlansCount = completedPlanCount,
            ongoingPlansCount = ongoingPlanCount,
            notStartedPlansCount = notStartedPlanCount,
            showAnalytics = showAnalytics
        )
    }.stateIn(
        scope = screenModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = DashboardUiState.Loading
    )

    @Composable
    override fun uiState(): DashboardUiState {
        return state.collectAsState().value
    }

    init {
        fetchData()
    }

    private fun fetchData() {
        screenModelScope.launch {
            planRepository.fetchPlans()
        }
    }
}