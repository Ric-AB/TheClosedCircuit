package com.closedcircuit.closedcircuitapplication.data.auth

import com.closedcircuit.closedcircuitapplication.core.network.ApiResponse
import com.closedcircuit.closedcircuitapplication.data.auth.dto.LoginRequest
import com.closedcircuit.closedcircuitapplication.data.auth.dto.LoginResponse
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.Headers
import de.jensklingenberg.ktorfit.http.POST

interface KtorfitAuthService {

    @Headers("Content-Type: application/json")
    @POST("user/login/")
    suspend fun loginWithEmailAndPassword(@Body loginRequest: LoginRequest): ApiResponse<LoginResponse>
}