package com.closedcircuit.closedcircuitapplication.domain.loan

import com.closedcircuit.closedcircuitapplication.domain.model.Avatar
import com.closedcircuit.closedcircuitapplication.domain.model.Date
import com.closedcircuit.closedcircuitapplication.domain.model.ID
import com.closedcircuit.closedcircuitapplication.domain.model.Name
import com.closedcircuit.closedcircuitapplication.domain.model.Amount

data class Loan(
    val loanId: ID,
    val sponsorAvatar: Avatar,
    val sponsorFullName: Name,
    val loanAmount: Amount,
    val gracePeriod: Int,
    val interestRate: Int,
    val createdAt: Date
)
