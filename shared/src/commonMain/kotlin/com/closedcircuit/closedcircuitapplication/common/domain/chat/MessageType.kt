package com.closedcircuit.closedcircuitapplication.common.domain.chat

enum class MessageType(val value: String) {
    TEXT("text");

    companion object {
        val map = values().associateBy { it.value }

        fun fromText(text: String) = map[text] ?: TEXT
    }
}