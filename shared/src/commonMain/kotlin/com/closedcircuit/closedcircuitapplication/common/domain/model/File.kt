package com.closedcircuit.closedcircuitapplication.common.domain.model

import com.closedcircuit.closedcircuitapplication.common.domain.model.ImageUrl

data class File(
    val url: ImageUrl,
    val title: String,
    val description: String
)
