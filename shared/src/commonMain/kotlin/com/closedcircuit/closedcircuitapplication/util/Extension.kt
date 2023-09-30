package com.closedcircuit.closedcircuitapplication.util

expect fun String.Companion.format(format: String, vararg args: Any?): String

expect fun randomUUID(): String

val String.Companion.Empty
    inline get() = ""