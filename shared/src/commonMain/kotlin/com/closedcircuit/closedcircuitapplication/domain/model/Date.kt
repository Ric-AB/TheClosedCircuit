package com.closedcircuit.closedcircuitapplication.domain.model

import com.closedcircuit.closedcircuitapplication.presentation.util.formatDate
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
@JvmInline
value class Date(val value: String) {

    private fun toLocalDateTime(): LocalDateTime =
        toInstant().toLocalDateTime(TimeZone.currentSystemDefault())

    fun toInstant(): Instant = Instant.parse(value)

    fun format(format: Format): String {
        return formatDate(toLocalDateTime(), format.format)
    }

    companion object {
        fun now(): Date {
            return Date(Clock.System.now().toString())
        }
    }

    enum class Format(val format: String) {
        hh_mm_a("hh:mm a"),
        dd_mm_yyyy("dd/MM/yyyy"),
        mmm_dd("MMM dd"),
        yy_mm_dd("yyyy-MM-dd"),
        dd_mmmm_yyyy("dd MMMM, yyyy"),
        dd_mmm_yyyy("dd MMM, yyyy"),
        MMMM_dd_yyyy("MMMM dd, yyyy"),
        dd_mm_yyyy_hh_mm_ss("yyyy-MM-dd'T'HH:mm:ss'Z'"),
        dd_mm_yyyy_hh_mm_ss_SSS("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    }

}