package com.closedcircuit.closedcircuitapplication.beneficiary.domain.sponsor

import com.closedcircuit.closedcircuitapplication.beneficiary.domain.model.Avatar
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.model.Name
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.model.Amount

data class Sponsor(
    val avatar: Avatar,
    val fullName: Name,
    val loanAmount: Amount
)
