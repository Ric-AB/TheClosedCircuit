package com.closedcircuit.closedcircuitapplication.data.user

import com.closedcircuit.closedcircuitapplication.core.network.ApiResponse
import com.closedcircuit.closedcircuitapplication.core.network.mapOnSuccess
import com.closedcircuit.closedcircuitapplication.data.user.dto.UserDashboardResponse
import com.closedcircuit.closedcircuitapplication.domain.user.User
import com.closedcircuit.closedcircuitapplication.domain.user.UserRepository
import io.github.xxfast.kstore.KStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class UserRepositoryImpl(
    private val userService: UserService,
    userStore: KStore<User>
) : UserRepository {

    override val userFlow = userStore.updates
    override suspend fun fetchUser(userId: String): ApiResponse<User> {
        return withContext(Dispatchers.IO) {
            userService.getUserDetails(userId).mapOnSuccess { it.toUser() }
        }
    }

    override suspend fun getCurrentUser(): User {
        return userFlow.filterNotNull().first()
    }

    override fun getCurrentUserFlow(): Flow<User> {
        return userFlow.filterNotNull()
    }

    override suspend fun getUserDashboard(): ApiResponse<UserDashboardResponse> {
        return withContext(Dispatchers.IO) {
            userService.getUserDashboard()
        }
    }
}