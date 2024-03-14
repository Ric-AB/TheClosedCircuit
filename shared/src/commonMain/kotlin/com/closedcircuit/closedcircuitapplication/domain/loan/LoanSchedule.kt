package com.closedcircuit.closedcircuitapplication.domain.loan

import com.closedcircuit.closedcircuitapplication.domain.model.Amount
import com.closedcircuit.closedcircuitapplication.domain.model.Date
import com.closedcircuit.closedcircuitapplication.domain.util.TypeWithStringProperties
import kotlinx.serialization.Serializable

@Serializable
data class LoanSchedule(
    val date: Date,
    val repaymentAmount: Amount,
    val status: String
) : TypeWithStringProperties {
    override val properties: List<String>
        get() = listOf(date.value, repaymentAmount.value.toString())
}
