package com.closedcircuit.closedcircuitapplication.domain.model

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
@JvmInline
value class Date(val value: String) {
    init {
        Instant.parse(value)
    }

    fun toLocalDateTime(): LocalDateTime = LocalDateTime.parse(value)

    fun toInstant(): Instant = Instant.parse(value)

}