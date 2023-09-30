package com.closedcircuit.closedcircuitapplication.util

import java.util.UUID

actual fun String.Companion.format(format: String, vararg args: Any?): String {
    return java.lang.String.format(format, args)
}

actual fun randomUUID() = UUID.randomUUID().toString()