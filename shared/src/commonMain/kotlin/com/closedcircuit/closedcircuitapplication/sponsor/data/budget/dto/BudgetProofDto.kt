package com.closedcircuit.closedcircuitapplication.sponsor.data.budget.dto

import com.closedcircuit.closedcircuitapplication.sponsor.data.model.FileDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BudgetProofDto(
    @SerialName("budget")
    val files: List<FileDto>,
    val id: String
)
