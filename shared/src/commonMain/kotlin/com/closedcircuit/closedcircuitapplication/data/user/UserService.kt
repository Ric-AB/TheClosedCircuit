package com.closedcircuit.closedcircuitapplication.data.user

import com.closedcircuit.closedcircuitapplication.core.network.ApiResponse
import com.closedcircuit.closedcircuitapplication.data.user.dto.ApiUser
import com.closedcircuit.closedcircuitapplication.data.user.dto.KycRequest
import com.closedcircuit.closedcircuitapplication.data.user.dto.UpdateUserRequest
import com.closedcircuit.closedcircuitapplication.data.user.dto.UserDashboardResponse
import com.closedcircuit.closedcircuitapplication.util.ClosedCircuitApiEndpoints.DASHBOARD
import com.closedcircuit.closedcircuitapplication.util.ClosedCircuitApiEndpoints.KYC
import com.closedcircuit.closedcircuitapplication.util.ClosedCircuitApiEndpoints.USER
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Headers
import de.jensklingenberg.ktorfit.http.PATCH
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.Path

interface UserService {
    @GET(USER)
    suspend fun getUserDetails(@Path("id") userId: String): ApiResponse<ApiUser>

    @GET(DASHBOARD)
    suspend fun getUserDashboard(): ApiResponse<UserDashboardResponse>

    @Headers("Content-Type: application/json")
    @PATCH(USER)
    suspend fun updateUser(
        @Body updateUserRequest: UpdateUserRequest,
        @Path("id") userId: String
    ): ApiResponse<ApiUser>

    @Headers("Content-Type: application/json")
    @POST(KYC)
    suspend fun verifyKyc(@Body kycRequest: KycRequest): ApiResponse<Unit>
}