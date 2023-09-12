package com.closedcircuit.closedcircuitapplication.data.user

import com.closedcircuit.closedcircuitapplication.core.network.ApiResponse
import com.closedcircuit.closedcircuitapplication.data.user.dto.UserDashboardResponse
import com.closedcircuit.closedcircuitapplication.data.user.dto.ApiUser
import com.closedcircuit.closedcircuitapplication.data.user.dto.UpdateUserRequest
import com.closedcircuit.closedcircuitapplication.data.util.ClosedCircuitApiEndpoints.DASHBOARD
import com.closedcircuit.closedcircuitapplication.data.util.ClosedCircuitApiEndpoints.UPDATE_USER
import com.closedcircuit.closedcircuitapplication.data.util.ClosedCircuitApiEndpoints.USER_DETAILS
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.PATCH
import de.jensklingenberg.ktorfit.http.Path

interface UserService {
    @GET(USER_DETAILS)
    suspend fun getUserDetails(
        @Path("id") userId: String
    ): ApiResponse<ApiUser>

    @GET(DASHBOARD)
    suspend fun getUserDashboard(): ApiResponse<UserDashboardResponse>

    @PATCH(UPDATE_USER)
    suspend fun updateUser(
        @Body updateUserRequest: UpdateUserRequest,
        @Path("id") userId: String
    ): ApiResponse<ApiUser>
}