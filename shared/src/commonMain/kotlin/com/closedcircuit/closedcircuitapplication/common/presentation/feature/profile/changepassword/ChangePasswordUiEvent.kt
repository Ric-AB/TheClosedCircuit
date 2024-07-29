package com.closedcircuit.closedcircuitapplication.common.presentation.feature.profile.changepassword

sealed interface ChangePasswordUiEvent {
    data class OldPasswordChange(val password: String) : ChangePasswordUiEvent
    data class NewPasswordChange(val password: String) : ChangePasswordUiEvent
    data class ConfirmPasswordChange(val password: String) : ChangePasswordUiEvent
    object Submit : ChangePasswordUiEvent
}