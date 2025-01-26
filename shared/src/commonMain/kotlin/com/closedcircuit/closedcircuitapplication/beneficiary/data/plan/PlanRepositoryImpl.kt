package com.closedcircuit.closedcircuitapplication.beneficiary.data.plan

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.closedcircuit.closedcircuitapplication.beneficiary.data.budget.toBudgets
import com.closedcircuit.closedcircuitapplication.beneficiary.data.fundrequest.toFundRequests
import com.closedcircuit.closedcircuitapplication.beneficiary.data.step.toSteps
import com.closedcircuit.closedcircuitapplication.common.domain.budget.BudgetRepository
import com.closedcircuit.closedcircuitapplication.common.domain.fundrequest.FundRequest
import com.closedcircuit.closedcircuitapplication.common.domain.fundrequest.FundRequestRepository
import com.closedcircuit.closedcircuitapplication.common.domain.model.Amount
import com.closedcircuit.closedcircuitapplication.common.domain.model.Currency
import com.closedcircuit.closedcircuitapplication.common.domain.model.Date
import com.closedcircuit.closedcircuitapplication.common.domain.model.FundType
import com.closedcircuit.closedcircuitapplication.common.domain.model.ID
import com.closedcircuit.closedcircuitapplication.common.domain.model.ImageUrl
import com.closedcircuit.closedcircuitapplication.common.domain.model.TaskDuration
import com.closedcircuit.closedcircuitapplication.common.domain.plan.Plan
import com.closedcircuit.closedcircuitapplication.common.domain.plan.PlanRepository
import com.closedcircuit.closedcircuitapplication.common.domain.plan.Plans
import com.closedcircuit.closedcircuitapplication.common.domain.step.StepRepository
import com.closedcircuit.closedcircuitapplication.common.util.orZero
import com.closedcircuit.closedcircuitapplication.core.network.ApiResponse
import com.closedcircuit.closedcircuitapplication.core.network.mapOnSuccess
import com.closedcircuit.closedcircuitapplication.database.TheClosedCircuitDatabase
import database.PlanEntity
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class PlanRepositoryImpl(
    database: TheClosedCircuitDatabase,
    private val planService: PlanService,
    private val stepRepository: StepRepository,
    private val budgetRepository: BudgetRepository,
    private val fundRequestRepository: FundRequestRepository,
    private val ioDispatcher: CoroutineDispatcher,
    private val defaultDispatcher: CoroutineDispatcher
) : PlanRepository {
    private val queries = database.planEntityQueries


    override fun getPlansAsFlow(): Flow<Plans> {
        return queries.getPlanEntities(mapper = ::mapToPlanWithFundRequest)
            .asFlow()
            .mapToList(defaultDispatcher)
    }

    // todo create use case for this?
    override suspend fun fetchPlans(): ApiResponse<Plans> {
        return withContext(ioDispatcher + NonCancellable) {
            planService.getPlans().mapOnSuccess { response ->
                val apiPlans = response.plans
                queries.transaction {
                    apiPlans.forEach { apiPlan ->
                        saveLocally(apiPlan.asPlanEntity())
                        stepRepository.saveStepLocally(apiPlan.steps.toSteps())
                        budgetRepository.saveBudgetLocally(apiPlan.budgets.toBudgets())
                        fundRequestRepository.saveFundRequestLocally(apiPlan.fundRequests.toFundRequests())
                    }
                }

                apiPlans.toPlans()
            }
        }
    }

    override suspend fun createPlan(plan: Plan): ApiResponse<Plan> {
        return withContext(ioDispatcher + NonCancellable) {
            planService.createPlan(plan.asRequest()).mapOnSuccess { apiPlan ->
                val planEntity = apiPlan.asPlanEntity()
                saveLocally(planEntity)
                planEntity.asPlan()
            }
        }
    }

    override suspend fun updatePlan(plan: Plan): ApiResponse<Plan> {
        return withContext(ioDispatcher + NonCancellable) {
            planService.updateUserPlan(planId = plan.id.value, request = plan.asRequest())
                .mapOnSuccess { apiPlan ->
                    val planEntity = apiPlan.asPlanEntity()
                    saveLocally(planEntity)
                    planEntity.asPlan()
                }
        }
    }

    override suspend fun deletePlan(id: ID): ApiResponse<Plan> {
        val idValue = id.value
        return withContext(ioDispatcher + NonCancellable) {
            planService.deletePlan(idValue).mapOnSuccess {
                val plan = queries.getPlanEntityByID(id = idValue, mapper = ::mapToPlanWithFundRequest)
                    .executeAsOne()

                queries.deletePlanEntity(idValue)
                plan
            }
        }
    }

    override fun getRecentPlans(limit: Int): Flow<Plans> {
        return getPlansAsFlow().map { it.take(limit).toImmutableList() }
    }

    override fun getPlanByIDAsFlow(id: ID): Flow<Plan?> {
        return queries.getPlanEntityByID(id = id.value, mapper = ::mapToPlanWithFundRequest)
            .asFlow()
            .mapToOneOrNull(ioDispatcher)
    }

    override suspend fun fetchPlanByID(id: ID): ApiResponse<Plan> {
        return planService.fetchPlanById(id.value).mapOnSuccess { it.asPlan() }
    }

    override suspend fun clear() {
        queries.deleteAll()
    }

    private fun mapToPlanWithFundRequest(
        id: String,
        avatar: String,
        category: String,
        sector: String,
        type: String?,
        name: String,
        description: String,
        duration: Long,
        estimatedSellingPrice: Double,
        estimatedCostPrice: Double,
        currency: String,
        fundsRaisedPercent: Double,
        tasksCompletedPercent: Double,
        targetAmount: Double?,
        totalFundsRaised: Double,
        analytics: String?,
        userID: String,
        hasRequestedFund: Boolean,
        isSponsored: Boolean,
        createdAt: String,
        updatedAt: String,
        id_: String?,
        planId: String?,
        beneficiaryId: String?,
        meansOfSupport: String?,
        minimumLoanRange: Double?,
        maximumLoanRange: Double?,
        maxLenders: Long?,
        currency_: String?,
        graceDuration: Long?,
        repaymentDuration: Long?,
        interestRate: Long?,
        createdAt_: String?,
        updatedAt_: String?,
    ): Plan {
        val currencyObj = Currency(currency)
        val lastFundRequest = mapToFundRequest(
            id = id_,
            planId = planId,
            beneficiaryId = beneficiaryId,
            meansOfSupport = meansOfSupport,
            minimumLoanRange = minimumLoanRange,
            maximumLoanRange = maximumLoanRange,
            maxLenders = maxLenders,
            currency = currencyObj,
            graceDuration = graceDuration,
            repaymentDuration = repaymentDuration,
            interestRate = interestRate,
            createdAt = createdAt_,
            updatedAt = updatedAt_
        )
        return Plan(
            id = ID(id),
            avatar = ImageUrl(avatar),
            category = category,
            sector = sector.replace("_", " "),
            type = type,
            name = name,
            description = description,
            duration = TaskDuration(duration),
            estimatedSellingPrice = Amount(estimatedSellingPrice, currencyObj),
            estimatedCostPrice = Amount(estimatedCostPrice, currencyObj),
            currency = currencyObj,
            fundsRaisedPercent = fundsRaisedPercent,
            tasksCompletedPercent = tasksCompletedPercent,
            targetAmount = Amount(targetAmount.orZero(), currencyObj),
            totalFundsRaised = Amount(totalFundsRaised, currencyObj),
            analytics = analytics.orEmpty(),
            lastFundRequest = lastFundRequest,
            userID = ID(userID),
            hasRequestedFund = hasRequestedFund,
            isSponsored = isSponsored,
            accountabilityPartners = emptyList()
        )
    }

    private fun mapToFundRequest(
        id: String?,
        planId: String?,
        beneficiaryId: String?,
        meansOfSupport: String?,
        minimumLoanRange: Double?,
        maximumLoanRange: Double?,
        maxLenders: Long?,
        currency: Currency?,
        graceDuration: Long?,
        repaymentDuration: Long?,
        interestRate: Long?,
        createdAt: String?,
        updatedAt: String?,
    ): FundRequest? {
        id ?: return null
        planId ?: return null
        beneficiaryId ?: return null

        return FundRequest(
            id = ID(id),
            planId = ID(planId),
            beneficiaryId = ID(beneficiaryId),
            fundType = FundType.fromText(meansOfSupport),
            minimumLoanRange = minimumLoanRange?.let { Amount(it, currency) },
            maximumLoanRange = maximumLoanRange?.let { Amount(it, currency) },
            currency = currency,
            maxLenders = maxLenders?.toInt(),
            graceDuration = graceDuration?.toInt(),
            repaymentDuration = repaymentDuration?.toInt(),
            interestRate = interestRate?.toInt(),
            createdAt = Date(createdAt.orEmpty()),
            updatedAt = Date(updatedAt.orEmpty())
        )
    }

    private fun saveLocally(planEntity: PlanEntity) {
        queries.upsertPlanEntity(planEntity)
    }
}