package com.closedcircuit.closedcircuitapplication.sponsor.domain.plan

import com.closedcircuit.closedcircuitapplication.common.domain.model.Amount
import com.closedcircuit.closedcircuitapplication.common.domain.model.ImageUrl
import com.closedcircuit.closedcircuitapplication.common.domain.model.Currency
import com.closedcircuit.closedcircuitapplication.common.domain.model.Date
import com.closedcircuit.closedcircuitapplication.common.domain.model.ID
import com.closedcircuit.closedcircuitapplication.common.domain.model.PositiveInt
import com.closedcircuit.closedcircuitapplication.sponsor.domain.step.FundedStep

data class FundedPlan(
    val id: ID,
    val avatar: ImageUrl,
    val name: String,
    val description: String,
    val category: String,
    val sector: String,
    val type: String,
    val duration: PositiveInt,
    val estimatedSellingPrice: Amount,
    val estimatedCostPrice: Amount,
    val targetAmount: Amount,
    val analytics: String?,
    val userID: ID,
    val beneficiaryId: ID,
    val fundRequestID: ID,
    val currency: Currency,
    val hasRequestedFunds: Boolean?,
    val isSponsored: Boolean?,
    val steps: List<FundedStep>,
    val accountabilityPartnerIds: List<ID>,
    val createdAt: Date,
    val updatedAt: Date
)
