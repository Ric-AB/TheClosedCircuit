package com.closedcircuit.closedcircuitapplication.beneficiary.domain.app

import kotlinx.serialization.Serializable

@Serializable
data class AppSettings(
    val hasOnboarded: Boolean = false
)
