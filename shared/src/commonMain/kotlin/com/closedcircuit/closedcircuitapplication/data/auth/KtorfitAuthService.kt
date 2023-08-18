package com.closedcircuit.closedcircuitapplication.data.auth

import com.closedcircuit.closedcircuitapplication.core.network.ApiResponse
import com.closedcircuit.closedcircuitapplication.data.auth.dto.GenerateOtpRequest
import com.closedcircuit.closedcircuitapplication.data.auth.dto.GenerateOtpResponse
import com.closedcircuit.closedcircuitapplication.data.auth.dto.LoginRequest
import com.closedcircuit.closedcircuitapplication.data.auth.dto.LoginResponse
import com.closedcircuit.closedcircuitapplication.data.auth.dto.RegisterRequest
import com.closedcircuit.closedcircuitapplication.data.auth.dto.RegisterResponse
import com.closedcircuit.closedcircuitapplication.data.auth.dto.ResetPasswordRequest
import com.closedcircuit.closedcircuitapplication.data.auth.dto.ResetPasswordResponse
import com.closedcircuit.closedcircuitapplication.data.auth.dto.VerifyOtpRequest
import com.closedcircuit.closedcircuitapplication.data.auth.dto.VerifyOtpResponse
import com.closedcircuit.closedcircuitapplication.data.util.ClosedCircuitApiEndpoints.GENERATE_OTP
import com.closedcircuit.closedcircuitapplication.data.util.ClosedCircuitApiEndpoints.LOGIN
import com.closedcircuit.closedcircuitapplication.data.util.ClosedCircuitApiEndpoints.REGISTER
import com.closedcircuit.closedcircuitapplication.data.util.ClosedCircuitApiEndpoints.RESET_PASSWORD
import com.closedcircuit.closedcircuitapplication.data.util.ClosedCircuitApiEndpoints.VERIFY_OTP
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.Headers
import de.jensklingenberg.ktorfit.http.POST

interface KtorfitAuthService {

    @Headers("Content-Type: application/json")
    @POST(LOGIN)
    suspend fun loginWithEmailAndPassword(@Body loginRequest: LoginRequest): ApiResponse<LoginResponse>

    @Headers("Content-Type: application/json")
    @POST(REGISTER)
    suspend fun register(@Body registerRequest: RegisterRequest): ApiResponse<RegisterResponse>

    @Headers("Content-Type: application/json")
    @POST(GENERATE_OTP)
    suspend fun generateOtp(@Body generateOtpRequest: GenerateOtpRequest): ApiResponse<GenerateOtpResponse>

    @Headers("Content-Type: application/json")
    @POST(VERIFY_OTP)
    suspend fun verifyOtp(@Body verifyOtpRequest: VerifyOtpRequest): ApiResponse<VerifyOtpResponse>

    @Headers("Content-Type: application/json")
    @POST(RESET_PASSWORD)
    suspend fun resetPassword(@Body resetPasswordRequest: ResetPasswordRequest): ApiResponse<ResetPasswordResponse>
}