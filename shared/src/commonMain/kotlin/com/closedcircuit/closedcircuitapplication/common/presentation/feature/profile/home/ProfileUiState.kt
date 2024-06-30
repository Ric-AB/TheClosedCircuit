package com.closedcircuit.closedcircuitapplication.common.presentation.feature.profile.home

import com.closedcircuit.closedcircuitapplication.common.domain.model.ImageUrl
import com.closedcircuit.closedcircuitapplication.common.domain.model.Country
import com.closedcircuit.closedcircuitapplication.common.domain.model.Email
import com.closedcircuit.closedcircuitapplication.common.domain.model.Name
import com.closedcircuit.closedcircuitapplication.common.domain.model.PhoneNumber
import com.closedcircuit.closedcircuitapplication.common.domain.model.KycStatus
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.user.User

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
    val avatar: ImageUrl,
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
