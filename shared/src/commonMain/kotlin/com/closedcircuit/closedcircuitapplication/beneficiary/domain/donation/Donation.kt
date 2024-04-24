package com.closedcircuit.closedcircuitapplication.beneficiary.domain.donation

import com.closedcircuit.closedcircuitapplication.beneficiary.domain.model.Avatar
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.model.ID
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.model.Name
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.model.Amount

class Donation(
    val id: ID,
    val sponsorAvatar: Avatar,
    val sponsorFullName: Name,
    val planName: String,
    val amount: Amount
)
