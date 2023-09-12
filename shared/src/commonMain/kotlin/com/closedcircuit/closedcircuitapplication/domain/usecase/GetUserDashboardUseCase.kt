package com.closedcircuit.closedcircuitapplication.domain.usecase

import com.closedcircuit.closedcircuitapplication.core.network.ApiResponse
import com.closedcircuit.closedcircuitapplication.core.network.mapOnSuccess
import com.closedcircuit.closedcircuitapplication.data.user.toSponsor
import com.closedcircuit.closedcircuitapplication.domain.user.UserDashboard
import com.closedcircuit.closedcircuitapplication.domain.user.UserRepository
import com.closedcircuit.closedcircuitapplication.domain.wallet.WalletRepository

class GetUserDashboardUseCase(
    private val userRepository: UserRepository,
    private val walletRepository: WalletRepository
) {

    suspend operator fun invoke(): ApiResponse<UserDashboard> {
        val responseFromServer = userRepository.getUserDashboard()
        val user = userRepository.nonNullUser()
        val wallet = walletRepository.getUserWallet()

        return responseFromServer.mapOnSuccess { userDashboardResponse ->
            val (onGoing, completed, notStarted) = userDashboardResponse.planStatus.planAnalytics
            UserDashboard(
                user = user,
                completedPlansCount = completed,
                ongoingPlansCount = onGoing,
                notStartedPlansCount = notStarted,
                totalFundsRaised = wallet.totalFunds,
                topSponsors = userDashboardResponse.topSponsors.map { it.toSponsor() }
            )
        }
    }
}