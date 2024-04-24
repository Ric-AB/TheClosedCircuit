package com.closedcircuit.closedcircuitapplication.beneficiary.domain.loan

import com.closedcircuit.closedcircuitapplication.beneficiary.domain.model.Avatar
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.model.ID
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.model.Amount
import kotlinx.serialization.Serializable

@Serializable
data class LoanPreview(
    val planId: ID,
    val planName: String,
    val totalSponsors: Int,
    val totalAmountOffered: Amount,
    val sponsorAvatars: List<Avatar>
)
