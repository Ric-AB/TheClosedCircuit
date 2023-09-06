package com.closedcircuit.closedcircuitapplication.data.user

import com.closedcircuit.closedcircuitapplication.core.network.ApiResponse
import com.closedcircuit.closedcircuitapplication.data.user.dto.UserDashboardResponse
import com.closedcircuit.closedcircuitapplication.data.user.dto.UserResponse
import com.closedcircuit.closedcircuitapplication.data.util.ClosedCircuitApiEndpoints.DASHBOARD
import com.closedcircuit.closedcircuitapplication.data.util.ClosedCircuitApiEndpoints.USER_DETAILS
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Path

interface UserService {
    @GET(USER_DETAILS)
    suspend fun getUserDetails(
        @Path("id") userId: String
    ): ApiResponse<UserResponse>

    @GET(DASHBOARD)
    suspend fun getUserDashboard(): ApiResponse<UserDashboardResponse>
}