package com.closedcircuit.closedcircuitapplication.domain.user

import com.closedcircuit.closedcircuitapplication.core.network.ApiResponse
import com.closedcircuit.closedcircuitapplication.data.user.dto.UserDashboardResponse
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    val userFlow: Flow<User?>
    suspend fun fetchUser(userId: String): ApiResponse<User>

    suspend fun getCurrentUser(): User

    fun getCurrentUserFlow(): Flow<User>

    suspend fun getUserDashboard(): ApiResponse<UserDashboardResponse>
}