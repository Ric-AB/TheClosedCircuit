package com.closedcircuit.closedcircuitapplication.sponsor.data.step.dto

import com.closedcircuit.closedcircuitapplication.sponsor.data.budget.dto.BudgetProofDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetStepProofResponse(
    val id: String,
    @SerialName("step_name")
    val stepName: String,
    val proofs: List<BudgetProofDto>
)
