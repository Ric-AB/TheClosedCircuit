package com.closedcircuit.closedcircuitapplication.common.domain.user

import com.closedcircuit.closedcircuitapplication.common.domain.model.KycDocumentType
import com.closedcircuit.closedcircuitapplication.core.network.ApiResponse
import kotlinx.coroutines.flow.StateFlow

interface UserRepository {
    val userFlow: StateFlow<User?>

    suspend fun fetchUser(userId: String): ApiResponse<User>

    suspend fun getCurrentUser(): User

    suspend fun updateUser(user: User): ApiResponse<User>

    suspend fun getUserDashboard(): ApiResponse<UserDashboard>

    suspend fun verifyKyc(
        documentType: KycDocumentType,
        documentNumber: String
    ): ApiResponse<Unit>

    suspend fun changePassword(
        oldPassword: String,
        newPassword: String,
        confirmPassword: String
    ): ApiResponse<Unit>

    suspend fun clear()
}