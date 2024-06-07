package com.closedcircuit.closedcircuitapplication.sponsor.data.plan

import com.closedcircuit.closedcircuitapplication.common.domain.model.ID
import com.closedcircuit.closedcircuitapplication.core.network.ApiResponse
import com.closedcircuit.closedcircuitapplication.core.network.mapOnSuccess
import com.closedcircuit.closedcircuitapplication.sponsor.data.plan.dto.GetSponsoredPlanDto
import com.closedcircuit.closedcircuitapplication.sponsor.domain.plan.DashboardPlan
import com.closedcircuit.closedcircuitapplication.sponsor.domain.plan.PlanRepository
import com.closedcircuit.closedcircuitapplication.sponsor.domain.plan.SponsorPlan
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PlanRepositoryImpl(
    private val noAuthPlanService: NoAuthPlanService,
    private val planService: PlanService,
    private val ioDispatcher: CoroutineDispatcher,
) : PlanRepository {

    override suspend fun fetchPlanByFundRequestId(fundRequestID: ID): ApiResponse<SponsorPlan> {
        return withContext(ioDispatcher) {
            noAuthPlanService.fetchPlanByFundRequestId(fundRequestID.value).mapOnSuccess {
                it.asSponsorPlan()
            }
        }
    }

    override suspend fun fetchSponsoredPlans(): ApiResponse<List<DashboardPlan>> {
        return withContext(ioDispatcher) {
            planService.fetchSponsoredPlans().mapOnSuccess {
                it.plans.toDashboardPlans()
            }
        }
    }
}