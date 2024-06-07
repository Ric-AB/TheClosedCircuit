package com.closedcircuit.closedcircuitapplication.sponsor.domain.loan

import com.closedcircuit.closedcircuitapplication.common.domain.model.Amount
import com.closedcircuit.closedcircuitapplication.common.domain.model.Avatar
import com.closedcircuit.closedcircuitapplication.common.domain.model.Date
import com.closedcircuit.closedcircuitapplication.common.domain.model.ID
import com.closedcircuit.closedcircuitapplication.common.domain.model.Name
import kotlinx.serialization.Serializable

@Serializable
data class LoanOffer(
    val loanOfferId: ID?,
    val businessName: String?,
    val beneficiaryName: Name?,
    val beneficiaryAvatar: Avatar?,
    val planSector: String?,
    val loanAmount: Amount?,
    val createdAt: Date?
)
