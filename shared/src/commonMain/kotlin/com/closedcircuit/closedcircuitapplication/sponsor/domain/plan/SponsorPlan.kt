package com.closedcircuit.closedcircuitapplication.sponsor.domain.plan

import com.closedcircuit.closedcircuitapplication.common.domain.budget.Budgets
import com.closedcircuit.closedcircuitapplication.common.domain.model.Amount
import com.closedcircuit.closedcircuitapplication.common.domain.model.Currency
import com.closedcircuit.closedcircuitapplication.common.domain.model.ID
import com.closedcircuit.closedcircuitapplication.common.domain.model.ImageUrl
import com.closedcircuit.closedcircuitapplication.common.domain.model.Name
import com.closedcircuit.closedcircuitapplication.common.domain.model.TaskDuration
import com.closedcircuit.closedcircuitapplication.common.domain.step.Steps
import com.closedcircuit.closedcircuitapplication.sponsor.domain.fundrequest.SponsorFundRequest
import kotlinx.serialization.Serializable

@Serializable
data class SponsorPlan(
    val id: ID,
    val avatar: ImageUrl,
    val category: String,
    val sector: String,
    val type: String?,
    val name: String,
    val description: String,
    val duration: TaskDuration,
    val estimatedSellingPrice: Amount,
    val estimatedCostPrice: Amount,
    val fundsRaised: Double,
    val tasksCompleted: Double,
    val targetAmount: Amount,
    val totalFundsRaised: Amount,
    val currency: Currency,
    val analytics: String,
    val userID: ID,
    val beneficiaryFullName: Name,
    val beneficiaryId: ID?,
    val fundRequest: SponsorFundRequest,
    val hasRequestedFund: Boolean,
    val isSponsored: Boolean,
    val steps: Steps,
    val budgets: Budgets
)
