package com.closedcircuit.closedcircuitapplication.common.domain.app

import com.closedcircuit.closedcircuitapplication.common.domain.model.ProfileType
import kotlinx.serialization.Serializable

@Serializable
data class AppSettings(
    val hasOnboarded: Boolean = false,
    val activeProfile: ProfileType = ProfileType.BENEFICIARY
)
