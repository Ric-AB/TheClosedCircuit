package com.closedcircuit.closedcircuitapplication.data.user.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class KycRequest(
    @SerialName("id_type")
    val idType: String,
    @SerialName("id_number")
    val idNumber: String,
    @SerialName("date_of_birth")
    val dateOfBirth: String?
)
