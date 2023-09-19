package com.closedcircuit.closedcircuitapplication.domain.plan

import com.closedcircuit.closedcircuitapplication.domain.model.Avatar
import com.closedcircuit.closedcircuitapplication.domain.model.ID
import com.closedcircuit.closedcircuitapplication.domain.model.Price
import com.closedcircuit.closedcircuitapplication.domain.model.TaskDuration

data class Plan(
    val id: ID,
    val avatar: Avatar,
    val category: String,
    val sector: String,
    val type: String?,
    val name: String,
    val description: String,
    val duration: TaskDuration,
    val estimatedSellingPrice: Price,
    val estimatedCostPrice: Price,
    val fundsRaised: Price,
    val tasksCompleted: String,
    val targetAmount: Price,
    val totalFundsRaised: Price,
    val planAnalytics: String,
    val userID: ID,
    val hasRequestedFund: Boolean,
    val isSponsored: Boolean,
    val accountabilityPartners: List<String>,
)
