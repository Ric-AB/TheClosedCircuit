package com.closedcircuit.closedcircuitapplication.sponsor.domain.plan

import com.closedcircuit.closedcircuitapplication.common.domain.model.Amount
import com.closedcircuit.closedcircuitapplication.common.domain.model.ImageUrl
import com.closedcircuit.closedcircuitapplication.common.domain.model.Currency
import com.closedcircuit.closedcircuitapplication.common.domain.model.Date
import com.closedcircuit.closedcircuitapplication.common.domain.model.FundType
import com.closedcircuit.closedcircuitapplication.common.domain.model.ID
import com.closedcircuit.closedcircuitapplication.common.domain.model.Name
import com.closedcircuit.closedcircuitapplication.sponsor.presentation.feature.makeoffer.FundingLevel

data class FundedPlanPreview(
    val id: ID,
    val avatar: ImageUrl,
    val sector: String,
    val beneficiaryFullName: Name,
    val beneficiaryId: ID?,
    val currency: Currency?,
    val amountFunded: Amount,
    val fundingType: FundType,
    val fundingLevel: FundingLevel,
    val fundingDate: Date,
    val fundsRaisedPercent: Double,
    val tasksCompletedPercent: Double
)
