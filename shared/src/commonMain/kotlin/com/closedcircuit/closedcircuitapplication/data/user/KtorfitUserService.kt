package com.closedcircuit.closedcircuitapplication.data.user

import com.closedcircuit.closedcircuitapplication.core.network.ApiResponse
import com.closedcircuit.closedcircuitapplication.data.user.dto.UserResponse
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Path

interface KtorfitUserService {
    @GET("user/manage-user/{id}/")
    suspend fun getUserDetails(
        @Path("id") userId: String
    ): ApiResponse<UserResponse>
}