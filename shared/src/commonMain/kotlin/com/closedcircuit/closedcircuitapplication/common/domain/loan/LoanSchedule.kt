package com.closedcircuit.closedcircuitapplication.common.domain.loan

import com.closedcircuit.closedcircuitapplication.common.domain.model.Amount
import com.closedcircuit.closedcircuitapplication.common.domain.model.Date
import com.closedcircuit.closedcircuitapplication.common.domain.util.TypeWithStringProperties
import kotlinx.serialization.Serializable

@Serializable
data class LoanSchedule(
    val date: Date,
    val repaymentAmount: Amount,
    val status: String
) : TypeWithStringProperties {
    override val properties: List<String>
        get() = listOf(date.format(Date.Format.dd_mmm_yyyy), repaymentAmount.value.toString())
}
