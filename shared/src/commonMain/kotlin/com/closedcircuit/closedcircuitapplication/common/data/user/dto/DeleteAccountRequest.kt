package com.closedcircuit.closedcircuitapplication.common.data.user.dto

import kotlinx.serialization.Serializable

@Serializable
data class DeleteAccountRequest(
    val email: String
)
