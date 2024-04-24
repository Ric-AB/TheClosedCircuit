package com.closedcircuit.closedcircuitapplication.beneficiary.domain.loan

import com.closedcircuit.closedcircuitapplication.beneficiary.domain.model.Amount
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.model.Avatar
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.model.Date
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.model.ID
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.model.LoanStatus
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.model.Name
import kotlinx.serialization.Serializable

@Serializable
data class LoanDetails(
    val loanID: ID,
    val interestAmount: Amount,
    val loanAmount: Amount,
    val repaymentAmount: Amount,
    val loanSchedule: List<LoanSchedule>?,
    val gracePeriod: Int,
    val sponsorFullName: Name,
    val sponsorAvatar: Avatar,
    val createdAt: Date,
    val updatedAt: Date,
    val fundingLevel: String,
    val otherAmount: Amount,
    val isDonation: Boolean,
    val status: LoanStatus?,
    val fundRequestID: ID
)