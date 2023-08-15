package com.closedcircuit.closedcircuitapplication.presentation.feature.authentication.login

data class LoginUIState(
    val email: String = "",
    val password: String = "",
    val loading: Boolean = false
)
