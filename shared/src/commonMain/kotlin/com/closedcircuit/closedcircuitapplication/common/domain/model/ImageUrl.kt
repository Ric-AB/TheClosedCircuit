package com.closedcircuit.closedcircuitapplication.common.domain.model

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
@JvmInline
value class ImageUrl(val value: String) {
    init {
        val regex =
            Regex("(@)?(href=')?(HREF=')?(HREF=\")?(href=\")?(http://)?[a-zA-Z_0-9\\-]+(\\.\\w[a-zA-Z_0-9\\-]+)+(/[#&\\n\\-=?+%/.\\w]+)?")

//https://firebasestorage.googleapis.com/v0/b/the-closed-circuit-85591.appspot.com/o/images%2F0e19a73f-1a87-411e-a2cc-118fd239f8b2.jpg?alt=media&token=83620656-fd75-4c64-9f31-804671e2ef5d
    }

    inline fun isEmpty() = value.isEmpty()
}