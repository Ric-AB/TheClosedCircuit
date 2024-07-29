package com.closedcircuit.closedcircuitapplication.common.data.auth

import com.closedcircuit.closedcircuitapplication.core.network.ApiResponse
import com.closedcircuit.closedcircuitapplication.core.network.mapOnSuccess
import com.closedcircuit.closedcircuitapplication.beneficiary.data.auth.dto.GenerateOtpRequest
import com.closedcircuit.closedcircuitapplication.beneficiary.data.auth.dto.LoginRequest
import com.closedcircuit.closedcircuitapplication.beneficiary.data.auth.dto.LoginResponse
import com.closedcircuit.closedcircuitapplication.common.data.auth.dto.RegisterRequest
import com.closedcircuit.closedcircuitapplication.beneficiary.data.auth.dto.ResetPasswordRequest
import com.closedcircuit.closedcircuitapplication.beneficiary.data.auth.dto.VerifyOtpRequest
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.auth.AuthenticationRepository
import com.closedcircuit.closedcircuitapplication.common.data.auth.dto.SaveFcmTokenRequest
import com.closedcircuit.closedcircuitapplication.core.network.ApiErrorResponse
import com.closedcircuit.closedcircuitapplication.core.network.ApiSuccessResponse
import com.closedcircuit.closedcircuitapplication.core.network.onError
import com.closedcircuit.closedcircuitapplication.core.network.onSuccess
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.FirebaseAuth
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.messaging.FirebaseMessaging
import dev.gitlive.firebase.messaging.messaging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock

class AuthenticationRepositoryImpl(
    private val authService: AuthService,
    private val firebaseAuth: FirebaseAuth = Firebase.auth,
    private val firebaseMessaging: FirebaseMessaging = Firebase.messaging
) : AuthenticationRepository {

    override suspend fun loginWithEmailAndPassword(
        email: String,
        password: String
    ): ApiResponse<LoginResponse> {
        return withContext(Dispatchers.IO) {
            val request = LoginRequest(email, password)
            var loginResult = authService.loginWithEmailAndPassword(request)

            loginResult.onSuccess {
                loginWithFirebaseCustomToken(it.firebaseCustomToken).onError { code, message ->
                    loginResult = ApiErrorResponse(message, code)
                }

//                saveFcmToken(it.userId, it.token).onError { code, message ->
//                    loginResult = ApiErrorResponse(message, code)
//                }
            }

            loginResult
        }
    }

    private suspend fun loginWithFirebaseCustomToken(firebaseToken: String): ApiResponse<Unit> {
        val authResult = firebaseAuth.signInWithCustomToken(firebaseToken)
        return if (authResult.user != null) ApiSuccessResponse(Unit)
        else ApiErrorResponse("Firebase login failed.", -1)
    }

    private suspend fun saveFcmToken(userId: String, bearerToken: String): ApiResponse<Unit> {
        return try {
            val token = firebaseMessaging.getToken()
            val request = SaveFcmTokenRequest(
                registrationToken = token,
                userId = userId,
                timestamp = Clock.System.now().toString()
            )

            val authHeader = "Bearer $bearerToken"
            authService.saveFcmToken(request, authHeader)
        } catch (e: Exception) {
            ApiErrorResponse(e.message.toString(), -1)
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