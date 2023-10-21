package com.closedcircuit.closedcircuitapplication.presentation.feature.authentication.login

sealed interface LoginUiEvent {
    data class EmailChange(val email: String) : LoginUiEvent
    data class PasswordChange(val password: String) : LoginUiEvent
    object Submit : LoginUiEvent
}