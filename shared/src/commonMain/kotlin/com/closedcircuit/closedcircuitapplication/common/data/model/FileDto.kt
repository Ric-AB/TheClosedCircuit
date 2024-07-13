package com.closedcircuit.closedcircuitapplication.common.data.model

import kotlinx.serialization.Serializable

@Serializable
data class FileDto(
    val url: String,
    val title: String,
    val description: String
)
