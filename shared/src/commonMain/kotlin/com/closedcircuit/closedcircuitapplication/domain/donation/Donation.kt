package com.closedcircuit.closedcircuitapplication.domain.donation

import com.closedcircuit.closedcircuitapplication.domain.model.Avatar
import com.closedcircuit.closedcircuitapplication.domain.model.ID
import com.closedcircuit.closedcircuitapplication.domain.model.Name
import com.closedcircuit.closedcircuitapplication.domain.model.Price

class Donation(
    val id: ID,
    val sponsorAvatar: Avatar,
    val sponsorFullName: Name,
    val planName: String,
    val amount: Price
)
