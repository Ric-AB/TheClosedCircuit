package com.closedcircuit.closedcircuitapplication.common.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BudgetProofDto(
    @SerialName("budget")
    val files: List<FileDto>,
    val id: String
)
