package com.closedcircuit.closedcircuitapplication.data.user

import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.Headers
import de.jensklingenberg.ktorfit.http.POST
import kotlinx.serialization.Serializable

interface KtorfitUserService {
    @Headers("Content-Type: application/json")
    @POST("https://theclosedcircuit-staging.herokuapp.com/api/user/login/")
    suspend fun loginWithEmailAndPassword(@Body loginRequest: LoginRequest): String
}
@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)