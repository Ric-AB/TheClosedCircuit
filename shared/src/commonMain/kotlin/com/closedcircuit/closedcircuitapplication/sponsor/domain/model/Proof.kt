package com.closedcircuit.closedcircuitapplication.sponsor.domain.model

import com.closedcircuit.closedcircuitapplication.common.domain.model.ID

data class Proof(
    val id: ID,
    val files: List<File>
)
