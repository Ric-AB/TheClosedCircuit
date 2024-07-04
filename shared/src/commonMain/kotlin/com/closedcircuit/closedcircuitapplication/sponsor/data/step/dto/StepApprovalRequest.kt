package com.closedcircuit.closedcircuitapplication.sponsor.data.step.dto

import kotlinx.serialization.Serializable

@Serializable
data class StepApprovalRequest(
    val status: String
)
