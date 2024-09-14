package com.closedcircuit.closedcircuitapplication.common.presentation.feature.profile.home

import androidx.compose.runtime.Immutable
import com.closedcircuit.closedcircuitapplication.common.domain.model.KycStatus

@Immutable
data class ProfileUIState(
    val imageUploadLoading: Boolean,
    val avatar: String,
    val firstName: String,
    val fullName: String,
    val email: String,
    val phoneNumber: String,
    val country: String,
    val isEmailVerified: Boolean,
    val phoneNumberStatus: KycStatus,
    val kycStatus: KycStatus,
)

sealed interface ProfileTabResult {
    data class ProfileUpdateError(val message: String) : ProfileTabResult
}

