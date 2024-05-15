package com.closedcircuit.closedcircuitapplication.common.domain.fundrequest

import com.closedcircuit.closedcircuitapplication.core.network.ApiResponse

interface FundRequestRepository {

    suspend fun createFundRequest(fundRequest: FundRequest): ApiResponse<FundRequest>
}