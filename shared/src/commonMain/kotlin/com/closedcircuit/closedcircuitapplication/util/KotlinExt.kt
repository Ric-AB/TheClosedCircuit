package com.closedcircuit.closedcircuitapplication.util

import androidx.compose.runtime.snapshots.SnapshotStateList

expect fun String.Companion.format(format: String, vararg args: Any?): String

expect fun randomUUID(): String

val String.Companion.Empty
    inline get() = ""

fun <T> SnapshotStateList<T>.replaceAll(items: Collection<T>) {
    clear()
    addAll(items)
}