package com.closedcircuit.closedcircuitapplication.domain.auth

import com.closedcircuit.closedcircuitapplication.core.network.ApiResponse
import com.closedcircuit.closedcircuitapplication.data.auth.dto.LoginResponse

interface AuthenticationRepository {
    suspend fun loginWithEmailAndPassword(
        email: String,
        password: String
    ): ApiResponse<LoginResponse>

    suspend fun register(
        fullName: String,
        email: String,
        roles: String,
        phoneNumber: String,
        password: String,
        confirmPassword: String
    ): ApiResponse<Unit>

    suspend fun requestOtp(email: String): ApiResponse<Unit>

    suspend fun verifyOtp(otpCode: String, email: String): ApiResponse<Unit>

    suspend fun resetPassword(
        otpCode: String,
        email: String,
        password: String,
        confirmPassword: String
    ): ApiResponse<Unit>
}