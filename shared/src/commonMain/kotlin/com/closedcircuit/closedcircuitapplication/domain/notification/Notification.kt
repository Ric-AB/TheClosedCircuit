package com.closedcircuit.closedcircuitapplication.domain.notification

import com.closedcircuit.closedcircuitapplication.domain.model.Avatar
import com.closedcircuit.closedcircuitapplication.domain.model.Date
import com.closedcircuit.closedcircuitapplication.domain.model.ID
import com.closedcircuit.closedcircuitapplication.domain.model.Name
import com.closedcircuit.closedcircuitapplication.domain.model.Price

data class Notification(
    val id: ID,
    val userID: ID,
    val fullName: Name,
    val message: String,
    val avatar: Avatar,
    val businessName: String,
    val amountOffered: Price,
    val isRead: Boolean,
    val createdAt: Date,
)
