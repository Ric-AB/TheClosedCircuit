package com.closedcircuit.closedcircuitapplication.common.domain.fundrequest

import com.closedcircuit.closedcircuitapplication.common.domain.model.ID
import com.closedcircuit.closedcircuitapplication.core.network.ApiResponse

interface FundRequestRepository {

    suspend fun createFundRequest(fundRequest: FundRequest): ApiResponse<FundRequest>

    fun saveFundRequestLocally(fundRequests: List<FundRequest>)

    suspend fun getLastFundRequestForPlan(planID: ID): FundRequest?
}