package com.closedcircuit.closedcircuitapplication.domain.user

import com.closedcircuit.closedcircuitapplication.core.network.ApiResponse
import com.closedcircuit.closedcircuitapplication.data.auth.dto.LoginResponse
import com.closedcircuit.closedcircuitapplication.data.auth.dto.RegisterResponse

interface UserRepository {

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

    suspend fun generateOtp(email: String): ApiResponse<Unit>

    suspend fun verifyOtp(otpCode: String, email: String): ApiResponse<Unit>

    suspend fun resetPassword(
        otpCode: String,
        email: String,
        password: String,
        confirmPassword: String
    ): ApiResponse<Unit>

    suspend fun getUser(userId: String): ApiResponse<User>
}