package com.closedcircuit.closedcircuitapplication.beneficiary.domain.donation

import com.closedcircuit.closedcircuitapplication.common.domain.model.ImageUrl
import com.closedcircuit.closedcircuitapplication.common.domain.model.ID
import com.closedcircuit.closedcircuitapplication.common.domain.model.Name
import com.closedcircuit.closedcircuitapplication.common.domain.model.Amount

class Donation(
    val id: ID,
    val sponsorAvatar: ImageUrl,
    val sponsorFullName: Name,
    val planName: String,
    val amount: Amount
)
