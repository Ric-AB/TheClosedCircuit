package com.closedcircuit.closedcircuitapplication.presentation.feature.authentication.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import com.closedcircuit.closedcircuitapplication.domain.user.UserRepository

class LoginViewModel(
    private val userRepository: UserRepository
) : ScreenModel {

    var state by mutableStateOf(LoginUIState())
        private set

    fun onEvent(event: LoginUIEvent) {
        when (event) {
            is LoginUIEvent.EmailChange -> updateEmail(event.email)
            is LoginUIEvent.PasswordChange -> updatePassword(event.password)
            LoginUIEvent.Submit -> attemptLogin()
        }
    }

    private fun attemptLogin() {

    }

    private fun updateEmail(email: String) {
        state = state.copy(email = email)
    }

    private fun updatePassword(password: String) {
        state = state.copy(password = password)
    }
}