package com.closedcircuit.closedcircuitapplication.common.data.model

import com.closedcircuit.closedcircuitapplication.common.domain.model.File
import com.closedcircuit.closedcircuitapplication.common.domain.model.ID
import com.closedcircuit.closedcircuitapplication.common.domain.model.ImageUrl
import com.closedcircuit.closedcircuitapplication.common.domain.model.Proof

fun ProofDto.toProof() = Proof(
    id = ID(id),
    files = files.map { it.toFile() }
)

fun FileDto.toFile() = File(
    url = ImageUrl(url),
    title = title,
    description = description
)