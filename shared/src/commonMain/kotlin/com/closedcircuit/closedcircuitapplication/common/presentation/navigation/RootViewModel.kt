package com.closedcircuit.closedcircuitapplication.common.presentation.navigation

import cafe.adriel.voyager.core.model.screenModelScope
import com.closedcircuit.closedcircuitapplication.common.domain.app.AppSettingsRepository
import com.closedcircuit.closedcircuitapplication.common.domain.model.AuthenticationState
import com.closedcircuit.closedcircuitapplication.common.domain.model.Currency
import com.closedcircuit.closedcircuitapplication.common.domain.model.Email
import com.closedcircuit.closedcircuitapplication.common.domain.model.ProfileType
import com.closedcircuit.closedcircuitapplication.common.domain.model.orDefault
import com.closedcircuit.closedcircuitapplication.common.domain.usecase.IsLoggedInUseCase
import com.closedcircuit.closedcircuitapplication.common.domain.usecase.LogoutUseCase
import com.closedcircuit.closedcircuitapplication.common.domain.user.UserRepository
import com.closedcircuit.closedcircuitapplication.common.presentation.util.BaseScreenModel
import com.closedcircuit.closedcircuitapplication.common.util.orFalse
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class RootViewModel(
    appSettingsRepository: AppSettingsRepository,
    userRepository: UserRepository,
    private val isLoggedInUseCase: IsLoggedInUseCase,
    private val logoutUseCase: LogoutUseCase,
) : BaseScreenModel<RootState, RootResult>() {

    private val authStateFlow = flow { emit(isLoggedInUseCase()) }
    val state = combine(
        authStateFlow,
        appSettingsRepository.getActiveProfileAsFlow(),
        userRepository.userFlow
    ) { authState, activeProfile, user ->
        RootState(
            authState = authState,
            activeProfile = activeProfile,
            fullName = user?.fullName?.value.orEmpty(),
            isEmailVerified = user?.isVerified.orFalse(),
            email = user?.email,
            profileUrl = user?.avatar?.value.orEmpty(),
            currency = user?.currency.orDefault()
        )
    }.stateIn(
        scope = screenModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = null
    )

    fun onEvent(event: RootEvent) {
        when (event) {
            RootEvent.Logout -> logout()
        }
    }

    private fun logout() {
        screenModelScope.launch {
            logoutUseCase()
            _resultChannel.send(RootResult.LogoutSuccess)
        }
    }
}

data class RootState(
    val authState: AuthenticationState,
    val activeProfile: ProfileType,
    val fullName: String,
    val profileUrl: String,
    val isEmailVerified: Boolean,
    val email: Email?,
    val currency: Currency
)

sealed interface RootEvent {
    object Logout : RootEvent
}

sealed interface RootResult {
    object LogoutSuccess : RootResult
}