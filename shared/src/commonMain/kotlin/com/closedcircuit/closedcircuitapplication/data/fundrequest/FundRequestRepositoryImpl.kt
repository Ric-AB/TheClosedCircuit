package com.closedcircuit.closedcircuitapplication.data.fundrequest

import com.closedcircuit.closedcircuitapplication.core.network.ApiResponse
import com.closedcircuit.closedcircuitapplication.core.network.mapOnSuccess
import com.closedcircuit.closedcircuitapplication.data.fundrequest.dto.ApiFundRequest
import com.closedcircuit.closedcircuitapplication.domain.fundrequest.FundRequest
import com.closedcircuit.closedcircuitapplication.domain.fundrequest.FundRequestRepository
import com.closedcircuit.closedcircuitapplication.domain.model.FundType
import com.closedcircuit.closedcircuitapplication.domain.model.Price

class FundRequestRepositoryImpl(private val service: FundRequestService) : FundRequestRepository {

    override suspend fun createFundRequest(fundRequest: FundRequest): ApiResponse<FundRequest> {
        return service.createFundRequest(fundRequest.toApiFundRequest())
            .mapOnSuccess { it.toFundRequest() }
    }
}