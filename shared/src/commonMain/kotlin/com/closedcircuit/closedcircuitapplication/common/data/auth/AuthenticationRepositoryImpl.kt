package com.closedcircuit.closedcircuitapplication.beneficiary.data.auth

import com.closedcircuit.closedcircuitapplication.core.network.ApiResponse
import com.closedcircuit.closedcircuitapplication.core.network.mapOnSuccess
import com.closedcircuit.closedcircuitapplication.beneficiary.data.auth.dto.GenerateOtpRequest
import com.closedcircuit.closedcircuitapplication.beneficiary.data.auth.dto.LoginRequest
import com.closedcircuit.closedcircuitapplication.beneficiary.data.auth.dto.LoginResponse
import com.closedcircuit.closedcircuitapplication.beneficiary.data.auth.dto.RegisterRequest
import com.closedcircuit.closedcircuitapplication.beneficiary.data.auth.dto.ResetPasswordRequest
import com.closedcircuit.closedcircuitapplication.beneficiary.data.auth.dto.VerifyOtpRequest
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.auth.AuthenticationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

class AuthenticationRepositoryImpl(
    private val authService: AuthService
) : AuthenticationRepository {

    override suspend fun loginWithEmailAndPassword(
        email: String,
        password: String
    ): ApiResponse<LoginResponse> {
        return withContext(Dispatchers.IO) {
            val request = LoginRequest(email, password)
            authService.loginWithEmailAndPassword(request)
        }
    }

    override suspend fun register(
        fullName: String,
        email: String,
        roles: String,
        phoneNumber: String,
        password: String,
        confirmPassword: String
    ): ApiResponse<Unit> {
        return withContext(Dispatchers.IO) {
            val request =
                RegisterRequest(email, fullName, roles, phoneNumber, password, confirmPassword)
            authService.register(request).mapOnSuccess { }
        }
    }

    override suspend fun requestOtp(email: String): ApiResponse<Unit> {
        return withContext(Dispatchers.IO) {
            val request = GenerateOtpRequest(email)
            authService.generateOtp(request).mapOnSuccess { }
        }
    }

    override suspend fun verifyOtp(otpCode: String, email: String): ApiResponse<Unit> {
        return withContext(Dispatchers.IO) {
            val request = VerifyOtpRequest(otpCode, email)
            authService.verifyOtp(request).mapOnSuccess { }
        }
    }

    override suspend fun resetPassword(
        otpCode: String,
        email: String,
        password: String,
        confirmPassword: String
    ): ApiResponse<Unit> {
        return withContext(Dispatchers.IO) {
            val request = ResetPasswordRequest(email, otpCode, password, confirmPassword)
            authService.resetPassword(request).mapOnSuccess { }
        }
    }
}