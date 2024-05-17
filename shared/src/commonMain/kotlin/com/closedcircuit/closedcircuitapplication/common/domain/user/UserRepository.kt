package com.closedcircuit.closedcircuitapplication.beneficiary.domain.user

import com.closedcircuit.closedcircuitapplication.core.network.ApiResponse
import com.closedcircuit.closedcircuitapplication.common.domain.model.KycDocumentType
import kotlinx.coroutines.flow.StateFlow

interface UserRepository {
    val userFlow: StateFlow<User?>
    suspend fun fetchUser(userId: String): ApiResponse<User>
    suspend fun nonNullUser(): User
    suspend fun updateUser(user: User): ApiResponse<User>
    suspend fun getUserDashboard(): ApiResponse<UserDashboard>
    suspend fun verifyKyc(
        documentType: KycDocumentType,
        documentNumber: String
    ): ApiResponse<Unit>
}