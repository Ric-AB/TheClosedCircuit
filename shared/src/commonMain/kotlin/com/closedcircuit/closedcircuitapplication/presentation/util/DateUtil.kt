package com.closedcircuit.closedcircuitapplication.presentation.util

import kotlinx.datetime.LocalDateTime

expect fun formatDate(localDateTime: LocalDateTime, format: String): String