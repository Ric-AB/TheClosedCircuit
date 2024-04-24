package com.closedcircuit.closedcircuitapplication.common.presentation.util

import kotlinx.datetime.LocalDateTime

expect fun formatDate(localDateTime: LocalDateTime, format: String): String