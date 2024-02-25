package com.closedcircuit.closedcircuitapplication.presentation.feature.profile.home

import com.closedcircuit.closedcircuitapplication.domain.model.Avatar
import com.closedcircuit.closedcircuitapplication.domain.model.Country
import com.closedcircuit.closedcircuitapplication.domain.model.Email
import com.closedcircuit.closedcircuitapplication.domain.model.Name
import com.closedcircuit.closedcircuitapplication.domain.model.PhoneNumber
import com.closedcircuit.closedcircuitapplication.domain.model.KycStatus
import com.closedcircuit.closedcircuitapplication.domain.user.User

data class ProfileUIState(
    val user: User,
) {
    companion object {
        fun init(user: User?): ProfileUIState? {
            if (user == null) return null

            return ProfileUIState(user = user)
        }
    }
}

data class PersonalData(
    val avatar: Avatar,
    val fullName: Name,
    val firstName: Name,
    val email: Email,
    val phoneNumber: PhoneNumber,
    val country: Country
)

data class AccountState(
    val emailVerified: Boolean,
    val documentStatus: KycStatus,
    val phoneNumberStatus: KycStatus
)

data class PlanSummary(
    val completedPlans: Int,
)
