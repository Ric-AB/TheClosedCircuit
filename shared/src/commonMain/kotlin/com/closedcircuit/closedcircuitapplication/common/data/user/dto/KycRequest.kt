package com.closedcircuit.closedcircuitapplication.beneficiary.data.user.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class KycRequest(
    @SerialName("id_type")
    val idType: String,
    @SerialName("id_number")
    val idNumber: String
)
