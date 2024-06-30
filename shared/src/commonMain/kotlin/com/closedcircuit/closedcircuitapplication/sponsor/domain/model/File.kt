package com.closedcircuit.closedcircuitapplication.sponsor.domain.model

import com.closedcircuit.closedcircuitapplication.common.domain.model.ImageUrl

data class File(
    val url: ImageUrl,
    val title: String,
    val description: String
)
