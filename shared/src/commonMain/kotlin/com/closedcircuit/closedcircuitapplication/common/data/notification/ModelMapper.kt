package com.closedcircuit.closedcircuitapplication.common.data.notification

import com.closedcircuit.closedcircuitapplication.common.data.notification.dto.ApiNotification
import com.closedcircuit.closedcircuitapplication.common.domain.model.ImageUrl
import com.closedcircuit.closedcircuitapplication.common.domain.model.Date
import com.closedcircuit.closedcircuitapplication.common.domain.model.ID
import com.closedcircuit.closedcircuitapplication.common.domain.model.Name
import com.closedcircuit.closedcircuitapplication.common.domain.model.Amount
import com.closedcircuit.closedcircuitapplication.common.domain.notification.Notification
import com.closedcircuit.closedcircuitapplication.common.domain.model.Currency
import kotlinx.collections.immutable.toImmutableList

fun ApiNotification.asNotification(): Notification {
    val currency = Currency(currency)
    return Notification(
        id = ID(id),
        userID = ID(userID),
        fullName = Name(fullName),
        message = message,
        avatar = ImageUrl(avatar),
        businessName = businessName,
        amountOffered = Amount(amountOffered, currency),
        currency = currency,
        isRead = isRead,
        createdAt = Date(createdAt)
    )
}

fun List<ApiNotification>.asNotifications() = this.map { it.asNotification() }.toImmutableList()