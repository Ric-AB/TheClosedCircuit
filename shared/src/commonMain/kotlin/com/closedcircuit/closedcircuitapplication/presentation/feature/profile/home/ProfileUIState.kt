package com.closedcircuit.closedcircuitapplication.presentation.feature.profile.home

import com.closedcircuit.closedcircuitapplication.domain.user.User

data class ProfileUIState(
    val user: User
) {
    companion object {
        fun init(user: User?): ProfileUIState? {
            if (user == null) return null

            return ProfileUIState(user = user)
        }
    }
}
