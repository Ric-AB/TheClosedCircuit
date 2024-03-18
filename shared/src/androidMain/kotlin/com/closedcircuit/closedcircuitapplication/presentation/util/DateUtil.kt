package com.closedcircuit.closedcircuitapplication.presentation.util

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaLocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

actual fun formatDate(localDateTime: LocalDateTime, format: String): String {
    return DateTimeFormatter
        .ofPattern(format, Locale.getDefault())
        .format(localDateTime.toJavaLocalDateTime())
}