package com.closedcircuit.closedcircuitapplication.common.data.user

import com.closedcircuit.closedcircuitapplication.beneficiary.data.user.dto.KycRequest
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.sponsor.Sponsor
import com.closedcircuit.closedcircuitapplication.common.data.user.dto.ChangePasswordRequest
import com.closedcircuit.closedcircuitapplication.common.data.user.dto.UpdateUserRequest
import com.closedcircuit.closedcircuitapplication.common.domain.chat.ChatUser
import com.closedcircuit.closedcircuitapplication.common.domain.country.CountryRepository
import com.closedcircuit.closedcircuitapplication.common.domain.model.Amount
import com.closedcircuit.closedcircuitapplication.common.domain.model.Currency
import com.closedcircuit.closedcircuitapplication.common.domain.model.ID
import com.closedcircuit.closedcircuitapplication.common.domain.model.ImageUrl
import com.closedcircuit.closedcircuitapplication.common.domain.model.KycDocumentType
import com.closedcircuit.closedcircuitapplication.common.domain.model.Name
import com.closedcircuit.closedcircuitapplication.common.domain.user.User
import com.closedcircuit.closedcircuitapplication.common.domain.user.UserDashboard
import com.closedcircuit.closedcircuitapplication.common.domain.user.UserRepository
import com.closedcircuit.closedcircuitapplication.common.util.Zero
import com.closedcircuit.closedcircuitapplication.core.network.ApiResponse
import com.closedcircuit.closedcircuitapplication.core.network.mapOnSuccess
import com.closedcircuit.closedcircuitapplication.core.storage.userStore
import io.github.xxfast.kstore.KStore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserRepositoryImpl(
    userStore: KStore<User>,
    private val userService: UserService,
    private val countryRepository: CountryRepository,
    private val ioDispatcher: CoroutineDispatcher
) : UserRepository {
    private val walletBalance = MutableStateFlow(Amount(Double.Zero))

    override val userFlow = combine(userStore.updates, walletBalance) { user, amount ->
        user?.copyWithWalletBalance(amount)
    }.stateIn(
        scope = CoroutineScope(ioDispatcher),
        started = SharingStarted.Eagerly,
        initialValue = null
    )

    override suspend fun fetchLoggedInUser(userId: ID): ApiResponse<User> {
        return withContext(ioDispatcher + NonCancellable) {
            userService.getUserDetails(userId.value).mapOnSuccess { apiUser ->
                val userCountry = countryRepository.findByName(apiUser.country)
                val user = apiUser.toUser(userCountry)
                saveLocally(user)
                user
            }
        }
    }

    override suspend fun fetchChatUser(userId: ID): ApiResponse<ChatUser> {
        return withContext(ioDispatcher) {
            userService.getUserDetails(userId.value).mapOnSuccess { apiUser ->
                apiUser.toChatUser()
            }
        }
    }

    override suspend fun getCurrentUser(): User {
        return userFlow.filterNotNull().first()
    }

    override suspend fun updateUser(user: User): ApiResponse<User> {
        return withContext(ioDispatcher + NonCancellable) {
            val apiUser = user.toApiUser()
            val request = UpdateUserRequest(
                fullName = apiUser.fullName,
                email = apiUser.email,
                phoneNumber = apiUser.phoneNumber,
                avatar = apiUser.avatar
            )

            userService.updateUser(request, apiUser.id).mapOnSuccess { apiUserFromServer ->
                val userCountry = countryRepository.findByName(apiUserFromServer.country)
                val updatedUser = apiUserFromServer.toUser(userCountry)
                saveLocally(updatedUser)
                updatedUser
            }
        }
    }

    override suspend fun getUserDashboard(): ApiResponse<UserDashboard> {
        return withContext(ioDispatcher) {
            userService.getUserDashboard().mapOnSuccess { response ->
                val currency = response.currency?.let { Currency(it) }
                val userDashboard = UserDashboard(
                    completedPlansCount = response.planStatus.planAnalytics.completed,
                    ongoingPlansCount = response.planStatus.planAnalytics.onGoing,
                    notStartedPlansCount = response.planStatus.planAnalytics.notStarted,
                    totalFundsRaised = Amount(response.totalFundsRaised.toDouble(), currency),
                    currency = currency,
                    topSponsors = response.topSponsors.map {
                        Sponsor(
                            avatar = ImageUrl(it.sponsorAvatar),
                            fullName = Name(it.sponsorFullName),
                            loanAmount = Amount(it.loanAmount, currency)
                        )
                    }
                )
                walletBalance.update { userDashboard.totalFundsRaised }
                userDashboard
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

    override suspend fun changePassword(
        oldPassword: String,
        newPassword: String,
        confirmPassword: String
    ): ApiResponse<Unit> {
        return withContext(ioDispatcher) {
            val userId = userFlow.value?.id?.value.orEmpty()
            val request = ChangePasswordRequest(
                oldPassword = oldPassword,
                newPassword = newPassword,
                confirmPassword = confirmPassword,
            )

            userService.changePassword(request, userId)
        }
    }

    override suspend fun clear() {
        userStore.reset()
    }

    private fun saveLocally(user: User) {
        CoroutineScope(ioDispatcher).launch {
            userStore.set(user)
        }
    }
}