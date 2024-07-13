package com.closedcircuit.closedcircuitapplication.sponsor.data.step

import com.closedcircuit.closedcircuitapplication.common.domain.model.ID
import com.closedcircuit.closedcircuitapplication.common.domain.model.ImageUrl
import com.closedcircuit.closedcircuitapplication.core.network.ApiResponse
import com.closedcircuit.closedcircuitapplication.core.network.mapOnSuccess
import com.closedcircuit.closedcircuitapplication.sponsor.data.step.dto.StepApprovalRequest
import com.closedcircuit.closedcircuitapplication.common.domain.model.File
import com.closedcircuit.closedcircuitapplication.common.domain.model.Proof
import com.closedcircuit.closedcircuitapplication.sponsor.domain.step.StepRepository

class StepRepositoryImpl(
    private val stepService: StepService
) : StepRepository {

    override suspend fun fetchStepProofs(stepId: ID): ApiResponse<List<Proof>> {
        return stepService.fetchStepProofs(stepId.value).mapOnSuccess { response ->
            response.proofs.map { proofDto ->
                Proof(
                    id = ID(proofDto.id),
                    files = proofDto.files.map {
                        File(
                            url = ImageUrl(it.url),
                            title = it.title,
                            description = it.description
                        )
                    }
                )
            }
        }
    }

    override suspend fun approveStep(stepId: ID): ApiResponse<Unit> {
        val request = StepApprovalRequest("approved")
        return stepService.approveStep(stepId.value, request)
    }
}