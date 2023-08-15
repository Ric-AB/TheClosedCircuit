package com.closedcircuit.closedcircuitapplication.data.user

import com.closedcircuit.closedcircuitapplication.core.network.ApiResponse
import com.closedcircuit.closedcircuitapplication.data.auth.KtorfitAuthService
import com.closedcircuit.closedcircuitapplication.data.auth.dto.LoginRequest
import com.closedcircuit.closedcircuitapplication.data.auth.dto.LoginResponse
import com.closedcircuit.closedcircuitapplication.domain.user.UserRepository
import io.github.aakira.napier.Napier
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
            val req = LoginRequest(email, password)
            authService.loginWithEmailAndPassword(req)
        }
    }

    override suspend fun getUser(userId: String) {
        val res = userService.getUserDetails(userId)
        Napier.d("User: $res")
    }
}