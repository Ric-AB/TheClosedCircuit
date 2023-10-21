package com.closedcircuit.closedcircuitapplication.data.user

import com.closedcircuit.closedcircuitapplication.core.network.ApiResponse
import com.closedcircuit.closedcircuitapplication.core.network.mapOnSuccess
import com.closedcircuit.closedcircuitapplication.core.storage.userStore
import com.closedcircuit.closedcircuitapplication.data.user.dto.KycRequest
import com.closedcircuit.closedcircuitapplication.data.user.dto.UpdateUserRequest
import com.closedcircuit.closedcircuitapplication.data.user.dto.UserDashboardResponse
import com.closedcircuit.closedcircuitapplication.domain.user.KycVerificationType
import com.closedcircuit.closedcircuitapplication.domain.user.User
import com.closedcircuit.closedcircuitapplication.domain.user.UserRepository
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
                updateUserLocally(user)
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
                updateUserLocally(updatedUser)
                updatedUser
            }
        }
    }

    override suspend fun getUserDashboard(): ApiResponse<UserDashboardResponse> {
        return withContext(ioDispatcher) {
            userService.getUserDashboard()
        }
    }

    override suspend fun verifyKyc(
        verificationType: KycVerificationType,
        verificationNumber: String
    ): ApiResponse<Unit> {
        return withContext(ioDispatcher) {
            val requestBody =
                KycRequest(idType = verificationType.name, idNumber = verificationNumber, dateOfBirth = null)
            userService.sendKycDetails(requestBody)
        }
    }

    private fun updateUserLocally(user: User) {
        CoroutineScope(ioDispatcher).launch {
            userStore.set(user)
        }
    }
}