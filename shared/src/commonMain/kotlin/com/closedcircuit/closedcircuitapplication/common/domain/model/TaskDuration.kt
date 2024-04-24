package com.closedcircuit.closedcircuitapplication.beneficiary.domain.model

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@JvmInline
@Serializable
value class TaskDuration(val value: Int) {

    constructor(value: Long) : this(value.toInt())

    init {
        require(value >= 0)
    }
}