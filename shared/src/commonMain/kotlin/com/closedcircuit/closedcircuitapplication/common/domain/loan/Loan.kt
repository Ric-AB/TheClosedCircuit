package com.closedcircuit.closedcircuitapplication.beneficiary.domain.loan

import com.closedcircuit.closedcircuitapplication.common.domain.model.ImageUrl
import com.closedcircuit.closedcircuitapplication.common.domain.model.Date
import com.closedcircuit.closedcircuitapplication.common.domain.model.ID
import com.closedcircuit.closedcircuitapplication.common.domain.model.Name
import com.closedcircuit.closedcircuitapplication.common.domain.model.Amount

data class Loan(
    val loanId: ID,
    val sponsorAvatar: ImageUrl,
    val sponsorFullName: Name,
    val loanAmount: Amount,
    val gracePeriod: Int,
    val interestRate: Int,
    val createdAt: Date
)
