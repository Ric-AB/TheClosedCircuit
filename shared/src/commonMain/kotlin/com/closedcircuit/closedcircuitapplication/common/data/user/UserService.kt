package com.closedcircuit.closedcircuitapplication.common.data.user

import com.closedcircuit.closedcircuitapplication.common.data.user.dto.ApiUser
import com.closedcircuit.closedcircuitapplication.beneficiary.data.user.dto.KycRequest
import com.closedcircuit.closedcircuitapplication.common.data.user.dto.ChangePasswordRequest
import com.closedcircuit.closedcircuitapplication.common.data.user.dto.DeleteAccountRequest
import com.closedcircuit.closedcircuitapplication.common.data.user.dto.UpdateUserRequest
import com.closedcircuit.closedcircuitapplication.common.data.user.dto.UserDashboardResponse
import com.closedcircuit.closedcircuitapplication.common.data.util.ClosedCircuitApiEndpoints.CHANGE_PASSWORD
import com.closedcircuit.closedcircuitapplication.common.data.util.ClosedCircuitApiEndpoints.DASHBOARD
import com.closedcircuit.closedcircuitapplication.common.data.util.ClosedCircuitApiEndpoints.DELETE_ACCOUNT
import com.closedcircuit.closedcircuitapplication.common.data.util.ClosedCircuitApiEndpoints.KYC
import com.closedcircuit.closedcircuitapplication.common.data.util.ClosedCircuitApiEndpoints.USER
import com.closedcircuit.closedcircuitapplication.core.network.ApiResponse
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Headers
import de.jensklingenberg.ktorfit.http.PATCH
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.PUT
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

    @Headers("Content-Type: application/json")
    @PUT(CHANGE_PASSWORD)
    suspend fun changePassword(
        @Body changePasswordRequest: ChangePasswordRequest,
        @Path("id") userId: String
    ): ApiResponse<Unit>

    @Headers("Content-Type: application/json")
    @POST(DELETE_ACCOUNT)
    suspend fun deleteAccount(
        @Path("id") userId: String,
        @Body deleteAccountRequest: DeleteAccountRequest
    ): ApiResponse<Unit>
}