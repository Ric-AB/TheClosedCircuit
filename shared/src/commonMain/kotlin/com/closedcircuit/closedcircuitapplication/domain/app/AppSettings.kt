package com.closedcircuit.closedcircuitapplication.domain.app

import kotlinx.serialization.Serializable

@Serializable
data class AppSettings(
    val hasOnboarded: Boolean = false
)
