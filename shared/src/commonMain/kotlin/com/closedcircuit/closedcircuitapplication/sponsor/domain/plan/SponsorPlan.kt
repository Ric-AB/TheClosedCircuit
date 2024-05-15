package com.closedcircuit.closedcircuitapplication.sponsor.domain.plan

import com.closedcircuit.closedcircuitapplication.beneficiary.domain.model.Amount
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.model.Avatar
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.model.ID
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.model.Name
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.model.TaskDuration
import com.closedcircuit.closedcircuitapplication.common.domain.fundrequest.FundRequest
import kotlinx.serialization.Serializable

@Serializable
data class SponsorPlan(
    val id: ID,
    val avatar: Avatar,
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
    val analytics: String,
    val userID: ID,
    val beneficiaryFullName: Name,
    val beneficiaryId: ID?,
    val fundRequest: FundRequest,
    val hasRequestedFund: Boolean,
    val isSponsored: Boolean,
)
