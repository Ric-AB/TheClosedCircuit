package com.closedcircuit.closedcircuitapplication.beneficiary.domain.loan

import com.closedcircuit.closedcircuitapplication.beneficiary.domain.model.Avatar
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.model.Date
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.model.ID
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.model.Name
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.model.Amount

data class Loan(
    val loanId: ID,
    val sponsorAvatar: Avatar,
    val sponsorFullName: Name,
    val loanAmount: Amount,
    val gracePeriod: Int,
    val interestRate: Int,
    val createdAt: Date
)