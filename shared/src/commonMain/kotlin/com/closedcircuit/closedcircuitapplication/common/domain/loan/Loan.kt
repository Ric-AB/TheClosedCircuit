package com.closedcircuit.closedcircuitapplication.common.domain.loan

import com.closedcircuit.closedcircuitapplication.common.domain.model.ImageUrl
import com.closedcircuit.closedcircuitapplication.common.domain.model.Date
import com.closedcircuit.closedcircuitapplication.common.domain.model.ID
import com.closedcircuit.closedcircuitapplication.common.domain.model.Name
import com.closedcircuit.closedcircuitapplication.common.domain.model.Amount
import com.closedcircuit.closedcircuitapplication.common.domain.model.Currency

data class Loan(
    val loanId: ID,
    val sponsorAvatar: ImageUrl,
    val sponsorFullName: Name,
    val loanAmount: Amount,
    val currency: Currency,
    val gracePeriod: Int,
    val interestRate: Int,
    val createdAt: Date
)
