package com.closedcircuit.closedcircuitapplication.data.user

import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Path

interface KtorfitUserService {
    @GET("user/manage-user/{id}/")
    suspend fun getUserDetails(
        @Path("id") userId: String
    ): String
}