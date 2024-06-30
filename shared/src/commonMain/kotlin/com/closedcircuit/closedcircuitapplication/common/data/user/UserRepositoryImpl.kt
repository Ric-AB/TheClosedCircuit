package com.closedcircuit.closedcircuitapplication.common.data.user

import com.closedcircuit.closedcircuitapplication.beneficiary.data.user.UserService
import com.closedcircuit.closedcircuitapplication.beneficiary.data.user.asApiUser
import com.closedcircuit.closedcircuitapplication.beneficiary.data.user.asUser
import com.closedcircuit.closedcircuitapplication.core.network.ApiResponse
import com.closedcircuit.closedcircuitapplication.core.network.mapOnSuccess
import com.closedcircuit.closedcircuitapplication.core.storage.userStore
import com.closedcircuit.closedcircuitapplication.beneficiary.data.user.dto.KycRequest
import com.closedcircuit.closedcircuitapplication.beneficiary.data.user.dto.UpdateUserRequest
import com.closedcircuit.closedcircuitapplication.common.domain.model.Amount
import com.closedcircuit.closedcircuitapplication.common.domain.model.ImageUrl
import com.closedcircuit.closedcircuitapplication.common.domain.model.KycDocumentType
import com.closedcircuit.closedcircuitapplication.common.domain.model.Name
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.sponsor.Sponsor
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.user.User
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.user.UserDashboard
import com.closedcircuit.closedcircuitapplication.common.domain.user.UserRepository
import io.github.xxfast.kstore.KStore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserRepositoryImpl(
    private val userService: UserService,
    userStore: KStore<User>,
    private val ioDispatcher: CoroutineDispatcher
) : UserRepository {

    override val userFlow = userStore.updates
        .stateIn(
            scope = CoroutineScope(ioDispatcher),
            started = SharingStarted.Eagerly,
            initialValue = null
        )

    override suspend fun fetchUser(userId: String): ApiResponse<User> {
        return withContext(ioDispatcher + NonCancellable) {
            userService.getUserDetails(userId).mapOnSuccess { apiUser ->
                val user = apiUser.asUser()
                saveLocally(user)
                user
            }
        }
    }

    override suspend fun nonNullUser(): User {
        return userFlow.filterNotNull().first()
    }

    override suspend fun updateUser(user: User): ApiResponse<User> {
        return withContext(ioDispatcher + NonCancellable) {
            val apiUser = user.asApiUser()
            val request = UpdateUserRequest(
                fullName = apiUser.fullName,
                email = apiUser.email,
                phoneNumber = apiUser.phoneNumber,
                avatar = apiUser.avatar
            )

            userService.updateUser(request, apiUser.id).mapOnSuccess { apiUserFromServer ->
                val updatedUser = apiUserFromServer.asUser()
                saveLocally(updatedUser)
                updatedUser
            }
        }
    }

    override suspend fun getUserDashboard(): ApiResponse<UserDashboard> {
        return withContext(ioDispatcher) {
            userService.getUserDashboard().mapOnSuccess { response ->
                UserDashboard(
                    completedPlansCount = response.planStatus.planAnalytics.completed,
                    ongoingPlansCount = response.planStatus.planAnalytics.onGoing,
                    notStartedPlansCount = response.planStatus.planAnalytics.notStarted,
                    totalFundsRaised = Amount(response.totalFundsRaised.toDouble()),
                    topSponsors = response.topSponsors.map {
                        Sponsor(
                            avatar = ImageUrl(it.sponsorAvatar),
                            fullName = Name(it.sponsorFullName),
                            loanAmount = Amount(it.loanAmount.toDouble())
                        )
                    }
                )
            }
        }
    }

    override suspend fun verifyKyc(
        documentType: KycDocumentType,
        documentNumber: String
    ): ApiResponse<Unit> {
        return withContext(ioDispatcher) {
            val requestBody = KycRequest(
                idType = documentType.name,
                idNumber = documentNumber
            )
            userService.verifyKyc(requestBody)
        }
    }

    private fun saveLocally(user: User) {
        CoroutineScope(ioDispatcher).launch {
            userStore.set(user)
        }
    }
}