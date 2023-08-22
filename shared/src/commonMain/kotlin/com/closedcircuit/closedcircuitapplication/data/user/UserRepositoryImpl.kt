package com.closedcircuit.closedcircuitapplication.data.user

import com.closedcircuit.closedcircuitapplication.core.network.ApiResponse
import com.closedcircuit.closedcircuitapplication.core.network.mapOnSuccess
import com.closedcircuit.closedcircuitapplication.data.auth.KtorfitAuthService
import com.closedcircuit.closedcircuitapplication.data.auth.dto.GenerateOtpRequest
import com.closedcircuit.closedcircuitapplication.data.auth.dto.LoginRequest
import com.closedcircuit.closedcircuitapplication.data.auth.dto.LoginResponse
import com.closedcircuit.closedcircuitapplication.data.auth.dto.RegisterRequest
import com.closedcircuit.closedcircuitapplication.data.auth.dto.ResetPasswordRequest
import com.closedcircuit.closedcircuitapplication.data.auth.dto.VerifyOtpRequest
import com.closedcircuit.closedcircuitapplication.domain.user.User
import com.closedcircuit.closedcircuitapplication.domain.user.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

class UserRepositoryImpl(
    private val userService: KtorfitUserService,
    private val authService: KtorfitAuthService
) : UserRepository {

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

    override suspend fun getUser(userId: String): ApiResponse<User> {
        return withContext(Dispatchers.IO) {
            userService.getUserDetails(userId).mapOnSuccess { it.toUser() }
        }
    }
}