package com.closedcircuit.closedcircuitapplication.presentation.feature.dashboard

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.closedcircuit.closedcircuitapplication.domain.donation.DonationRepository
import com.closedcircuit.closedcircuitapplication.domain.plan.PlanRepository
import com.closedcircuit.closedcircuitapplication.domain.usecase.GetUserDashboardUseCase
import com.closedcircuit.closedcircuitapplication.domain.user.UserRepository
import com.closedcircuit.closedcircuitapplication.domain.wallet.WalletRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val getUserDashboardUseCase: GetUserDashboardUseCase,
    private val userRepository: UserRepository,
    private val walletRepository: WalletRepository,
    private val planRepository: PlanRepository,
    private val donationRepository: DonationRepository
) : ScreenModel {

    val state =
        combine(
            userRepository.userFlow,
            walletRepository.walletFlow,
            planRepository.allPlansFlow,
            donationRepository.recentDonationsFlow
        ) { user, wallet, allPlans, recentDonations ->
            DashboardUIState(
                currentUser = user,
                wallet = wallet,
                recentPlans = allPlans,
                recentDonation = recentDonations
            )
        }.stateIn(coroutineScope, SharingStarted.WhileSubscribed(5_000L), DashboardUIState())

    init {
        fetchData()
    }

    fun onEvent(event: DashboardUIEvent) {
        when (event) {
            else -> {}
        }
    }

    private fun fetchData() {
        coroutineScope.launch {
        }
    }
}