package com.closedcircuit.closedcircuitapplication.common.presentation.util

import kotlinx.cinterop.convert
import kotlinx.datetime.LocalDateTime
import platform.Foundation.NSCalendar
import platform.Foundation.NSDate
import platform.Foundation.NSDateComponents
import platform.Foundation.NSDateFormatter
import platform.Foundation.NSLocale
import platform.Foundation.currentLocale

actual fun formatDate(localDateTime: LocalDateTime, format: String): String {
    val date = localDateTime.toNsDate()
        ?: throw IllegalStateException("Failed to convert LocalDateTime $LocalDateTime to NSDate")
    val formatter = NSDateFormatter().apply {
        dateFormat = format
        locale = NSLocale.currentLocale
    }
    return formatter.stringFromDate(date)
}

private fun LocalDateTime.toNsDate(): NSDate? {
    val calendar = NSCalendar.currentCalendar
    val components = NSDateComponents()
    components.year = this.year.convert()
    components.month = this.monthNumber.convert()
    components.day = this.dayOfMonth.convert()
    components.hour = this.hour.convert()
    components.minute = this.minute.convert()
    components.second = this.second.convert()
    return calendar.dateFromComponents(components)
}