package com.closedcircuit.closedcircuitapplication.util

expect fun String.Companion.format(format: String, vararg args: Any?): String

val String.Companion.Empty
    inline get() = ""