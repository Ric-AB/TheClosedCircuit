package com.closedcircuit.closedcircuitapplication.common.presentation.feature.profile.home

import com.closedcircuit.closedcircuitapplication.common.domain.user.User
import com.closedcircuit.closedcircuitapplication.common.domain.model.KycStatus

data class ProfileUIState(
    val avatar: String,
    val firstName: String,
    val fullName: String,
    val email: String,
    val phoneNumber: String,
    val country: String,
    val isEmailVerified: Boolean,
    val phoneNumberStatus: KycStatus,
    val kycStatus: KycStatus,
) {
    companion object {
        fun init(user: User?): ProfileUIState? {
            if (user == null) return null

            return ProfileUIState(
                avatar = user.avatar.value,
                firstName = user.firstName.value,
                fullName = user.fullName.value,
                email = user.email.value,
                phoneNumber = user.phoneNumber.phoneWithCode,
                country = user.country.name,
                isEmailVerified = user.isVerified,
                phoneNumberStatus = user.phoneNumberStatus,
                kycStatus = user.kycStatus,
            )
        }
    }
}

