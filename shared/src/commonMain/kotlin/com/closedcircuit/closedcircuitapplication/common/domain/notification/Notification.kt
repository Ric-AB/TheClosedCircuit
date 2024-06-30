package com.closedcircuit.closedcircuitapplication.beneficiary.domain.notification

import com.closedcircuit.closedcircuitapplication.common.domain.model.ImageUrl
import com.closedcircuit.closedcircuitapplication.common.domain.model.Date
import com.closedcircuit.closedcircuitapplication.common.domain.model.ID
import com.closedcircuit.closedcircuitapplication.common.domain.model.Name
import com.closedcircuit.closedcircuitapplication.common.domain.model.Amount

data class Notification(
    val id: ID,
    val userID: ID,
    val fullName: Name,
    val message: String,
    val avatar: ImageUrl,
    val businessName: String,
    val amountOffered: Amount,
    val isRead: Boolean,
    val createdAt: Date,
)
