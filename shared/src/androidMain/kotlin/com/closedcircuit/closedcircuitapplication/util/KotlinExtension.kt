package com.closedcircuit.closedcircuitapplication.util

actual fun String.Companion.format(format: String, vararg args: Any?): String {
    return java.lang.String.format(format, args)
}