package com.closedcircuit.closedcircuitapplication.sponsor.domain.step

import com.closedcircuit.closedcircuitapplication.common.domain.model.Amount
import com.closedcircuit.closedcircuitapplication.common.domain.model.Date
import com.closedcircuit.closedcircuitapplication.common.domain.model.ID
import com.closedcircuit.closedcircuitapplication.common.domain.model.PositiveInt
import com.closedcircuit.closedcircuitapplication.sponsor.domain.budget.FundedBudget

data class FundedStep(
    val id: ID,
    val planId: ID,
    val userId: ID,
    val name: String,
    val description: String,
    val duration: PositiveInt,
    val targetFunds: Amount,
    val totalFundsRaised: Amount,
    val isSponsored: Boolean,
    val status: String,
    val budgets: List<FundedBudget>,
    val approverIds: List<ID>,
    val createdAt: Date,
    val updatedAt: Date
)
