package com.closedcircuit.closedcircuitapplication.domain.session

import kotlinx.serialization.Serializable

@Serializable
data class Session(
    val lastLogin: String,
    val token: String,
    val firebaseCustomToken: String,
    val fcmServerKey: String
)
