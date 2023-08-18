package com.closedcircuit.closedcircuitapplication.presentation.feature.authentication.register

data class RegisterUIState(
    val firstName: String = "",
    val nickName: String = "",
    val lastName: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val loading: Boolean = false,
    val registerResult: RegisterResult? = null
)

sealed interface RegisterResult {
    object Success : RegisterResult
    data class Failure(val message: String) : RegisterResult
}
