package com.closedcircuit.closedcircuitapplication.common.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Proof(
    val id: ID,
    val files: List<File>
)
