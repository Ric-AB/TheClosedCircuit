package com.closedcircuit.closedcircuitapplication.domain.sponsor

import com.closedcircuit.closedcircuitapplication.domain.model.Avatar
import com.closedcircuit.closedcircuitapplication.domain.model.Name
import com.closedcircuit.closedcircuitapplication.domain.model.Amount

data class Sponsor(
    val avatar: Avatar,
    val fullName: Name,
    val loanAmount: Amount
)
