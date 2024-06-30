package com.closedcircuit.closedcircuitapplication.sponsor.domain.budget

import com.closedcircuit.closedcircuitapplication.common.domain.model.Amount
import com.closedcircuit.closedcircuitapplication.common.domain.model.Date
import com.closedcircuit.closedcircuitapplication.common.domain.model.ID

data class FundedBudget(
    val  id: ID,
    val planId: ID,
    val stepId: ID,
    val userId: ID,
    val name: String,
    val description: String,
    val cost: Amount,
    val isSponsored: Boolean,
    val fundsRaised: Amount,
    val isFunded: Boolean,
    val approverIds: List<ID>,
    val createdAt: Date,
    val updatedAt: Date
)
