package com.closedcircuit.closedcircuitapplication.beneficiary.data.fundrequest

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.closedcircuit.closedcircuitapplication.common.domain.fundrequest.FundRequest
import com.closedcircuit.closedcircuitapplication.common.domain.fundrequest.FundRequestRepository
import com.closedcircuit.closedcircuitapplication.common.domain.model.ID
import com.closedcircuit.closedcircuitapplication.core.network.ApiResponse
import com.closedcircuit.closedcircuitapplication.core.network.mapOnSuccess
import com.closedcircuit.closedcircuitapplication.database.TheClosedCircuitDatabase
import database.FundRequestEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FundRequestRepositoryImpl(
    theClosedCircuitDatabase: TheClosedCircuitDatabase,
    private val service: FundRequestService,
    private val defaultDispatcher: CoroutineDispatcher
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

    override fun getLastFundRequestForPlanAsFlow(planID: ID): Flow<FundRequest?> {
        return queries.getLastFundRequestForPlan(planID.value).asFlow()
            .mapToOneOrNull(defaultDispatcher)
            .map { it?.toFundRequest() }
    }

    override fun getAllFundRequestsAscendingAsFlow(): Flow<List<FundRequest>> {
        return queries.getAllFundRequestsAscending().asFlow()
            .mapToList(defaultDispatcher)
            .map { it.toFundRequests() }
    }

    override suspend fun clear() {
        queries.deleteAll()
    }

    private fun saveLocally(fundRequestEntity: FundRequestEntity) {
        queries.upsertFundRequestEntity(fundRequestEntity)
    }
}