package com.closedcircuit.closedcircuitapplication.sponsor.data.budget.dto

import com.closedcircuit.closedcircuitapplication.sponsor.data.model.DocumentDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BudgetProofDto(
    @SerialName("budget")
    val documents: List<DocumentDto>,
    val id: String
)
