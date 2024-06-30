package com.closedcircuit.closedcircuitapplication.beneficiary.domain.sponsor

import com.closedcircuit.closedcircuitapplication.common.domain.model.ImageUrl
import com.closedcircuit.closedcircuitapplication.common.domain.model.Name
import com.closedcircuit.closedcircuitapplication.common.domain.model.Amount

data class Sponsor(
    val avatar: ImageUrl,
    val fullName: Name,
    val loanAmount: Amount
)
