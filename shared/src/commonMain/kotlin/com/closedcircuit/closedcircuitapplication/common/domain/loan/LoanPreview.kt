package com.closedcircuit.closedcircuitapplication.common.domain.loan

import com.closedcircuit.closedcircuitapplication.common.domain.model.ImageUrl
import com.closedcircuit.closedcircuitapplication.common.domain.model.ID
import com.closedcircuit.closedcircuitapplication.common.domain.model.Amount
import com.closedcircuit.closedcircuitapplication.common.domain.model.Currency
import kotlinx.serialization.Serializable

@Serializable
data class LoanPreview(
    val planId: ID,
    val planName: String,
    val totalSponsors: Int,
    val totalAmountOffered: Amount,
    val currency: Currency,
    val sponsorAvatars: List<ImageUrl>
)
