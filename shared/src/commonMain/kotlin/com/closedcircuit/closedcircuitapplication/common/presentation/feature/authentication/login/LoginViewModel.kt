package com.closedcircuit.closedcircuitapplication.common.presentation.feature.authentication.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.closedcircuit.closedcircuitapplication.core.network.onError
import com.closedcircuit.closedcircuitapplication.core.network.onSuccess
import com.closedcircuit.closedcircuitapplication.common.domain.usecase.LoginUseCase
import com.closedcircuit.closedcircuitapplication.common.domain.app.AppSettingsRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.launch

class LoginViewModel(
    private val loginUseCase: LoginUseCase,
    private val appSettingsRepository: AppSettingsRepository
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
        screenModelScope.launch {
            state = state.copy(isLoading = true)

            val email = state.emailField.value
            val password = state.passwordField.value
            loginUseCase(email, password).onSuccess {
                val activeProfile = appSettingsRepository.getActiveProfile()
                state = state.copy(isLoading = false)
                _loginResultChannel.send(LoginResult.Success(activeProfile))
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