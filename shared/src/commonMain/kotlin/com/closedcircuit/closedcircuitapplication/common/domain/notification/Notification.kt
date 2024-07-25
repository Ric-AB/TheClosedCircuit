package com.closedcircuit.closedcircuitapplication.common.domain.notification

import com.closedcircuit.closedcircuitapplication.common.domain.model.ImageUrl
import com.closedcircuit.closedcircuitapplication.common.domain.model.Date
import com.closedcircuit.closedcircuitapplication.common.domain.model.ID
import com.closedcircuit.closedcircuitapplication.common.domain.model.Name
import com.closedcircuit.closedcircuitapplication.common.domain.model.Amount
import com.closedcircuit.closedcircuitapplication.common.domain.model.Currency

data class Notification(
    val id: ID,
    val userID: ID,
    val fullName: Name,
    val message: String,
    val avatar: ImageUrl,
    val businessName: String,
    val amountOffered: Amount,
    val currency: Currency,
    val isRead: Boolean,
    val createdAt: Date,
)
