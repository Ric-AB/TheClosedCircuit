package com.closedcircuit.closedcircuitapplication.common.domain.fundrequest

import com.closedcircuit.closedcircuitapplication.common.domain.model.ID
import com.closedcircuit.closedcircuitapplication.core.network.ApiResponse
import kotlinx.coroutines.flow.Flow

interface FundRequestRepository {

    suspend fun createFundRequest(fundRequest: FundRequest): ApiResponse<FundRequest>

    fun saveFundRequestLocally(fundRequests: List<FundRequest>)

    fun getLastFundRequestForPlanAsFlow(planID: ID): Flow<FundRequest?>

    fun getAllFundRequestsAscendingAsFlow(): Flow<List<FundRequest>>
}