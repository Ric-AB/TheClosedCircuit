package com.closedcircuit.closedcircuitapplication.data.step.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateOrUpdateStepRequest(
    @SerialName("step_name")
    val name: String,
    @SerialName("step_description")
    val description: String,
    val duration: String,
    @SerialName("target_funds")
    val targetFunds: Int,
    @SerialName("plan")
    val planID: String,
    @SerialName("user")
    val userID: String
)
