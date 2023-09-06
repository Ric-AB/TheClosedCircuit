package com.closedcircuit.closedcircuitapplication.domain.model

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@JvmInline
@Serializable
value class TaskDuration(val value: Int) {

    init {
        require(value > 0)
    }
}