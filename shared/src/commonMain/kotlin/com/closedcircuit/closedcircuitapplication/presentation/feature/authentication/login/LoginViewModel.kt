package com.closedcircuit.closedcircuitapplication.presentation.feature.authentication.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.closedcircuit.closedcircuitapplication.core.network.onError
import com.closedcircuit.closedcircuitapplication.core.network.onSuccess
import com.closedcircuit.closedcircuitapplication.domain.usecase.LoginUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginUseCase: LoginUseCase
) : ScreenModel {

    var state by mutableStateOf(LoginUIState())
        private set

    private val _loginResultChannel = Channel<LoginResult>()
    val loginResultChannel: ReceiveChannel<LoginResult> = _loginResultChannel


    fun onEvent(event: LoginUiEvent) {
        when (event) {
            is LoginUiEvent.EmailChange -> updateEmail(event.email)
            is LoginUiEvent.PasswordChange -> updatePassword(event.password)
            LoginUiEvent.Submit -> attemptLogin()
        }
    }

    private fun attemptLogin() {
        coroutineScope.launch {
            state = state.copy(isLoading = true)

            val email = state.emailField.value
            val password = state.passwordField.value
            loginUseCase(email, password).onSuccess {
                state = state.copy(isLoading = false)
                _loginResultChannel.send(LoginResult.Success)
            }.onError { _, message ->
                state = state.copy(isLoading = false)
                _loginResultChannel.send(LoginResult.Failure(message))
            }
        }
    }

    private fun updateEmail(email: String) {
        state.emailField.onValueChange(email)
    }

    private fun updatePassword(password: String) {
        state.passwordField.onValueChange(password)
    }
}