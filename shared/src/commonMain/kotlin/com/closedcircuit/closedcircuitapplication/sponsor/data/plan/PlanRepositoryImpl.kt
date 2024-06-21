package com.closedcircuit.closedcircuitapplication.sponsor.data.plan

import com.closedcircuit.closedcircuitapplication.common.domain.model.ID
import com.closedcircuit.closedcircuitapplication.core.network.ApiResponse
import com.closedcircuit.closedcircuitapplication.core.network.mapOnSuccess
import com.closedcircuit.closedcircuitapplication.sponsor.domain.plan.FundedPlan
import com.closedcircuit.closedcircuitapplication.sponsor.domain.plan.FundedPlanPreview
import com.closedcircuit.closedcircuitapplication.sponsor.domain.plan.PlanRepository
import com.closedcircuit.closedcircuitapplication.sponsor.domain.plan.SponsorPlan
import kotlinx.coroutines.CoroutineDispatcher

class PlanRepositoryImpl(
    private val noAuthPlanService: NoAuthPlanService,
    private val planService: PlanService,
    private val ioDispatcher: CoroutineDispatcher,
) : PlanRepository {

    override suspend fun fetchPlanByFundRequestId(fundRequestID: ID): ApiResponse<SponsorPlan> {
        return noAuthPlanService.fetchPlanByFundRequestId(fundRequestID.value).mapOnSuccess {
            it.asSponsorPlan()
        }
    }

    override suspend fun fetchFundedPlans(): ApiResponse<List<FundedPlanPreview>> {
        return planService.fetchFundedPlans().mapOnSuccess {
            it.plans.toDashboardPlans()
        }
    }

    override suspend fun fetchFundedPlanDetails(id: ID): ApiResponse<FundedPlan> {
        return planService.fetchFundedPlanDetails(id.value).mapOnSuccess {
            it.toFundedPlan()
        }
    }
}