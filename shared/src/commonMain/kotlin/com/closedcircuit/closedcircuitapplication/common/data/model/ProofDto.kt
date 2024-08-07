package com.closedcircuit.closedcircuitapplication.common.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProofDto(
    @SerialName("budget")
    val files: List<FileDto>,
    val id: String
)
