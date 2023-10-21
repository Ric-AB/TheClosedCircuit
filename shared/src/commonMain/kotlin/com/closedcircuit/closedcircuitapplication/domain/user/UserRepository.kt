package com.closedcircuit.closedcircuitapplication.domain.user

import com.closedcircuit.closedcircuitapplication.core.network.ApiResponse
import com.closedcircuit.closedcircuitapplication.data.user.dto.UserDashboardResponse
import kotlinx.coroutines.flow.StateFlow

interface UserRepository {
    val userFlow: StateFlow<User?>
    suspend fun fetchUser(userId: String): ApiResponse<User>
    suspend fun nonNullUser(): User
    suspend fun updateUser(user: User): ApiResponse<User>
    suspend fun getUserDashboard(): ApiResponse<UserDashboardResponse>
    suspend fun verifyKyc(verificationType: KycVerificationType, verificationNumber: String): ApiResponse<Unit>
}