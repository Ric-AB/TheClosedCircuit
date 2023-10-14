package com.closedcircuit.closedcircuitapplication.data.notification

import com.closedcircuit.closedcircuitapplication.data.notification.dto.ApiNotification
import com.closedcircuit.closedcircuitapplication.domain.model.Avatar
import com.closedcircuit.closedcircuitapplication.domain.model.Date
import com.closedcircuit.closedcircuitapplication.domain.model.ID
import com.closedcircuit.closedcircuitapplication.domain.model.Name
import com.closedcircuit.closedcircuitapplication.domain.model.Price
import com.closedcircuit.closedcircuitapplication.domain.notification.Notification
import kotlinx.collections.immutable.toImmutableList

fun ApiNotification.asNotification() = Notification(
    id = ID(id),
    userID = ID(userID),
    fullName = Name(fullName),
    message = message,
    avatar = Avatar(avatar),
    businessName = businessName,
    amountOffered = Price(amountOffered),
    isRead = isRead,
    createdAt = Date(createdAt)
)

fun List<ApiNotification>.asNotifications() = this.map { it.asNotification() }.toImmutableList()