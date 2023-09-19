package com.closedcircuit.closedcircuitapplication.domain.step

import com.closedcircuit.closedcircuitapplication.domain.model.Date
import com.closedcircuit.closedcircuitapplication.domain.model.ID
import com.closedcircuit.closedcircuitapplication.domain.model.Name
import com.closedcircuit.closedcircuitapplication.domain.model.Price
import com.closedcircuit.closedcircuitapplication.domain.model.TaskDuration

data class Step(
    val id: ID,
    val name: String,
    val description: String,
    val duration: TaskDuration,
    val targetFunds: Price,
    val totalFundsRaised: Price,
    val planID: ID,
    val userID: ID,
    val isSponsored: Boolean,
    val status: String,
    val createdAt: Date,
    val updatedAt: Date
)
