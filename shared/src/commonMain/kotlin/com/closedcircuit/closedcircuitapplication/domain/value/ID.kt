package com.closedcircuit.closedcircuitapplication.domain.value

import kotlin.jvm.JvmInline

@JvmInline
value class ID(val value: String) {
    init {
        require(value.isNotEmpty())
    }
}