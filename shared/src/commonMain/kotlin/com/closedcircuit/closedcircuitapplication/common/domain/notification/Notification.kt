package com.closedcircuit.closedcircuitapplication.beneficiary.domain.notification

import com.closedcircuit.closedcircuitapplication.beneficiary.domain.model.Avatar
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.model.Date
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.model.ID
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.model.Name
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.model.Amount

data class Notification(
    val id: ID,
    val userID: ID,
    val fullName: Name,
    val message: String,
    val avatar: Avatar,
    val businessName: String,
    val amountOffered: Amount,
    val isRead: Boolean,
    val createdAt: Date,
)
