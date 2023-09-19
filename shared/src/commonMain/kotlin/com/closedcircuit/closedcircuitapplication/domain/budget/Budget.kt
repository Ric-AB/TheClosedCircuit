package com.closedcircuit.closedcircuitapplication.domain.budget

import com.closedcircuit.closedcircuitapplication.domain.model.Date
import com.closedcircuit.closedcircuitapplication.domain.model.ID
import com.closedcircuit.closedcircuitapplication.domain.model.Price

data class Budget(
    val id: ID,
    val planID: ID,
    val stepID: ID,
    val userID: ID,
    val name: String,
    val description: String,
    val cost: Price,
    val isSponsored: Boolean,
    val fundsRaised: Price,
    val isCompleted: Boolean,
    val approvers: List<String>,
    val createdAt: Date,
    val updatedAt: Date
)
