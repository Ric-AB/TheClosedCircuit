package com.closedcircuit.closedcircuitapplication.beneficiary.data.fundrequest

import com.closedcircuit.closedcircuitapplication.core.network.ApiResponse
import com.closedcircuit.closedcircuitapplication.core.network.mapOnSuccess
import com.closedcircuit.closedcircuitapplication.common.domain.fundrequest.FundRequest
import com.closedcircuit.closedcircuitapplication.common.domain.fundrequest.FundRequestRepository

class FundRequestRepositoryImpl(private val service: FundRequestService) : FundRequestRepository {

    override suspend fun createFundRequest(fundRequest: FundRequest): ApiResponse<FundRequest> {
        return service.createFundRequest(fundRequest.toApiFundRequest())
            .mapOnSuccess { it.toFundRequest() }
    }
}