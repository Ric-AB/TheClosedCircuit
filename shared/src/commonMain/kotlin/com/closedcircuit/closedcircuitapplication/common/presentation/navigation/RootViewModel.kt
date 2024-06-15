package com.closedcircuit.closedcircuitapplication.common.presentation.navigation

import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.closedcircuit.closedcircuitapplication.common.domain.usecase.IsLoggedInUseCase
import com.closedcircuit.closedcircuitapplication.common.domain.app.AppSettingsRepository
import com.closedcircuit.closedcircuitapplication.common.domain.model.AuthenticationState
import com.closedcircuit.closedcircuitapplication.common.domain.model.ProfileType
import kotlinx.coroutines.launch

class RootViewModel(
    private val appSettingsRepository: AppSettingsRepository,
    private val isLoggedInUseCase: IsLoggedInUseCase
) : ScreenModel {

    val state = mutableStateOf<RootState?>(null)

    init {
        initRootState()
    }

    private fun initRootState() {
        screenModelScope.launch {
            val authState = isLoggedInUseCase()
            val activeProfileType = appSettingsRepository.getActiveProfile()
            state.value = RootState(
                activeProfileType = activeProfileType,
                authState = authState
            )
        }
    }
}

data class RootState(
    val authState: AuthenticationState,
    val activeProfileType: ProfileType
)