package com.closedcircuit.closedcircuitapplication.common.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class File(
    val url: ImageUrl,
    val title: String,
    val description: String
)
