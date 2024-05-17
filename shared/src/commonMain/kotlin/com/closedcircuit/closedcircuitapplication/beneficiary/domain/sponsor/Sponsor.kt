package com.closedcircuit.closedcircuitapplication.beneficiary.domain.sponsor

import com.closedcircuit.closedcircuitapplication.common.domain.model.Avatar
import com.closedcircuit.closedcircuitapplication.common.domain.model.Name
import com.closedcircuit.closedcircuitapplication.common.domain.model.Amount

data class Sponsor(
    val avatar: Avatar,
    val fullName: Name,
    val loanAmount: Amount
)
