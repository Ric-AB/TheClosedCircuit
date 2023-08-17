package com.closedcircuit.closedcircuitapplication.presentation.feature.authentication.login

data class LoginUIState(
    val email: String = "",
    val password: String = "",
    val loading: Boolean = false,
    val loginResult: LoginResult? = null
)

sealed interface LoginResult {
    object Success : LoginResult
    data class Failure(val message: String) : LoginResult
}
