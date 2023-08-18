package com.closedcircuit.closedcircuitapplication.presentation.feature.authentication.login

sealed interface LoginUIEvent {
    data class EmailChange(val email: String) : LoginUIEvent
    data class PasswordChange(val password: String) : LoginUIEvent
    object Submit : LoginUIEvent

    object LoginResultHandled : LoginUIEvent
}