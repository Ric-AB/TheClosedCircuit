package com.closedcircuit.closedcircuitapplication.beneficiary.data.fundrequest

import com.closedcircuit.closedcircuitapplication.core.network.ApiResponse
import com.closedcircuit.closedcircuitapplication.core.network.mapOnSuccess
import com.closedcircuit.closedcircuitapplication.common.domain.fundrequest.FundRequest
import com.closedcircuit.closedcircuitapplication.common.domain.fundrequest.FundRequestRepository
import com.closedcircuit.closedcircuitapplication.common.domain.model.ID
import com.closedcircuit.closedcircuitapplication.database.TheClosedCircuitDatabase
import database.FundRequestEntity

class FundRequestRepositoryImpl(
    theClosedCircuitDatabase: TheClosedCircuitDatabase,
    private val service: FundRequestService
) : FundRequestRepository {
    private val queries = theClosedCircuitDatabase.fundRequestEntityQueries

    override suspend fun createFundRequest(fundRequest: FundRequest): ApiResponse<FundRequest> {
        return service.createFundRequest(fundRequest.toApiFundRequest())
            .mapOnSuccess { it.toFundRequest() }
    }

    override fun saveFundRequestLocally(fundRequests: List<FundRequest>) {
        queries.transaction {
            fundRequests.forEach { saveLocally(it.toFundRequestEntity()) }
        }
    }

    override suspend fun getLastFundRequestForPlan(planID: ID): FundRequest? {
        return queries.getLastFundRequestForPlan(planID.value).executeAsOneOrNull()
            ?.toFundRequest()
    }

    private fun saveLocally(fundRequestEntity: FundRequestEntity) {
        queries.upsertFundRequestEntity(fundRequestEntity)
    }
}