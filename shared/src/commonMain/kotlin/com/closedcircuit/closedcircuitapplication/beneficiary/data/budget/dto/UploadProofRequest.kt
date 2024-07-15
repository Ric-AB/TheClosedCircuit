package com.closedcircuit.closedcircuitapplication.beneficiary.data.budget.dto

import com.closedcircuit.closedcircuitapplication.common.domain.model.File
import kotlinx.serialization.Serializable

@Serializable
data class UploadProofRequest(
    val proof: List<File>
)
