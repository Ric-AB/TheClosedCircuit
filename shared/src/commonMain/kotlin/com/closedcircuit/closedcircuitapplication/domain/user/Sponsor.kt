package com.closedcircuit.closedcircuitapplication.domain.user

import com.closedcircuit.closedcircuitapplication.domain.model.Avatar
import com.closedcircuit.closedcircuitapplication.domain.model.Name
import com.closedcircuit.closedcircuitapplication.domain.model.Price

data class Sponsor(
    val avatar: Avatar,
    val fullName: Name,
    val loanAmount: Price
)
