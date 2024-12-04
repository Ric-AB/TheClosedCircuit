package com.closedcircuit.closedcircuitapplication.common.domain.model

import com.closedcircuit.closedcircuitapplication.core.serialization.JavaSerializable
import kotlinx.serialization.Serializable

@Serializable
data class File(
    val url: ImageUrl,
    val title: String,
    val description: String
): JavaSerializable
