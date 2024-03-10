package com.closedcircuit.closedcircuitapplication.domain.loan

import com.closedcircuit.closedcircuitapplication.domain.model.Avatar
import com.closedcircuit.closedcircuitapplication.domain.model.ID
import com.closedcircuit.closedcircuitapplication.domain.model.Price
import kotlinx.serialization.Serializable

@Serializable
data class LoanPreview(
    val planId: ID,
    val planName: String,
    val totalSponsors: Int,
    val totalAmountOffered: Price,
    val sponsorAvatars: List<Avatar>
)
