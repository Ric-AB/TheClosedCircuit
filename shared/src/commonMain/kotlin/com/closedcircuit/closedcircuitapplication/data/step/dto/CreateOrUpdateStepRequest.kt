package com.closedcircuit.closedcircuitapplication.data.step.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateOrUpdateStepRequest(
    @SerialName("step_name")
    val name: String,
    @SerialName("step_description")
    val description: String,
    val duration: Int,
    @SerialName("target_funds")
    val targetFunds: Double,
    @SerialName("plan")
    val planID: String,
    @SerialName("user")
    val userID: String
)
