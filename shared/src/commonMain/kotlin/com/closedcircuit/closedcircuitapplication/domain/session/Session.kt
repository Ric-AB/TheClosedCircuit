package com.closedcircuit.closedcircuitapplication.domain.session

import com.closedcircuit.closedcircuitapplication.domain.value.Date
import com.closedcircuit.closedcircuitapplication.domain.value.Token
import com.closedcircuit.closedcircuitapplication.presentation.navigation.AuthenticationState
import kotlinx.datetime.Clock
import kotlinx.serialization.Serializable

@Serializable
data class Session(
    val lastLogin: Date,
    val token: Token,
    val firebaseCustomToken: Token,
    val fcmServerKey: Token
) {

    companion object {
        const val sessionDurationInHours = 6
    }

    fun currentAuthenticationState(hasOnboarded: Boolean): AuthenticationState {
        if (!hasOnboarded) return AuthenticationState.FIRST_TIME

        val lastLoginAsLocalDateTime = lastLogin.toInstant()
        val now = Clock.System.now()
        val differenceInHours = now - lastLoginAsLocalDateTime
        return if (differenceInHours.inWholeHours <= sessionDurationInHours) AuthenticationState.LOGGED_IN
        else AuthenticationState.LOGGED_OUT
    }
}
