package com.closedcircuit.closedcircuitapplication.common.presentation.feature.profile.changepassword

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.screenModelScope
import com.closedcircuit.closedcircuitapplication.common.domain.user.UserRepository
import com.closedcircuit.closedcircuitapplication.common.presentation.util.BaseScreenModel
import com.closedcircuit.closedcircuitapplication.core.network.onComplete
import com.closedcircuit.closedcircuitapplication.core.network.onError
import com.closedcircuit.closedcircuitapplication.core.network.onSuccess
import kotlinx.coroutines.launch

class ChangePasswordViewModel(
    private val userRepository: UserRepository
) : BaseScreenModel<ChangePasswordUiState, ChangePasswordResult>() {

    var state by mutableStateOf(ChangePasswordUiState())

    fun onEvent(event: ChangePasswordUiEvent) {
        when (event) {
            is ChangePasswordUiEvent.ConfirmPasswordChange -> updateConfirmPassword(event.password)
            is ChangePasswordUiEvent.NewPasswordChange -> updateNewPassword(event.password)
            is ChangePasswordUiEvent.OldPasswordChange -> updateOldPassword(event.password)
            ChangePasswordUiEvent.Submit -> attemptChangePassword()
        }
    }

    private fun attemptChangePassword() {
        if (areFieldsValid()) {
            state = state.copy(isLoading = true)

            val oldPassword = state.oldPasswordField.value
            val newPassword = state.newPasswordField.value
            val confirmPassword = state.confirmPasswordField.value
            screenModelScope.launch {
                userRepository.changePassword(oldPassword, newPassword, confirmPassword)
                    .onComplete {
                        state = state.copy(isLoading = false)
                    }.onSuccess {
                        _resultChannel.send(ChangePasswordResult.ChangePasswordSuccess)
                    }.onError { _, message ->
                        _resultChannel.send(ChangePasswordResult.ChangePasswordError(message))
                    }
            }
        }
    }

    private fun updateConfirmPassword(password: String) {
        state.confirmPasswordField.onValueChange(password)
        validateConfirmPassword()
    }

    private fun updateNewPassword(password: String) {
        state.newPasswordField.onValueChange(password)
    }

    private fun updateOldPassword(password: String) {
        state.oldPasswordField.onValueChange(password)
    }

    private fun areFieldsValid(): Boolean {
        val fields = state.fieldsToValidate
        fields.forEach { inputField -> inputField.validateInput() }
        validateConfirmPassword()
        return fields.all { inputField -> !inputField.isError }
    }

    private fun validateConfirmPassword() {
        if (state.confirmPasswordField.value != state.newPasswordField.value) {
            state.confirmPasswordField.error = "Passwords do not match"
        } else {
            state.confirmPasswordField.error = ""
        }
    }
}