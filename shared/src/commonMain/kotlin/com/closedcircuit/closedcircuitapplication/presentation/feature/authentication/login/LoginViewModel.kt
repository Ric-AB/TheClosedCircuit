package com.closedcircuit.closedcircuitapplication.presentation.feature.authentication.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.closedcircuit.closedcircuitapplication.domain.usecase.LoginUseCase
import com.closedcircuit.closedcircuitapplication.domain.user.UserRepository
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginUseCase: LoginUseCase
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
        coroutineScope.launch {
            state = state.copy(loading = true)
            loginUseCase(state.email, state.password)
            state = state.copy(loading = false)
        }
    }

    private fun updateEmail(email: String) {
        state = state.copy(email = email)
    }

    private fun updatePassword(password: String) {
        state = state.copy(password = password)
    }
}