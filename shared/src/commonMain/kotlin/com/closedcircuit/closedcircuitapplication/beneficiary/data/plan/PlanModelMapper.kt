package com.closedcircuit.closedcircuitapplication.beneficiary.data.plan

import com.closedcircuit.closedcircuitapplication.beneficiary.data.plan.dto.ApiPlan
import com.closedcircuit.closedcircuitapplication.beneficiary.data.plan.dto.SavePlanPayload
import com.closedcircuit.closedcircuitapplication.common.domain.model.Amount
import com.closedcircuit.closedcircuitapplication.common.domain.model.Currency
import com.closedcircuit.closedcircuitapplication.common.domain.model.Date
import com.closedcircuit.closedcircuitapplication.common.domain.model.ID
import com.closedcircuit.closedcircuitapplication.common.domain.model.ImageUrl
import com.closedcircuit.closedcircuitapplication.common.domain.model.TaskDuration
import com.closedcircuit.closedcircuitapplication.common.domain.plan.Plan
import com.closedcircuit.closedcircuitapplication.common.util.orFalse
import com.closedcircuit.closedcircuitapplication.common.util.orZero
import database.PlanEntity
import kotlin.jvm.JvmName

fun PlanEntity.asPlan(): Plan {
    val currency = Currency(currency)
    return Plan(
        id = ID(id),
        avatar = ImageUrl(avatar),
        category = category,
        sector = sector,
        type = type,
        name = name,
        description = description,
        duration = TaskDuration(duration),
        estimatedSellingPrice = Amount(estimatedSellingPrice, currency),
        estimatedCostPrice = Amount(estimatedCostPrice, currency),
        currency = currency,
        fundsRaisedPercent = fundsRaisedPercent,
        tasksCompletedPercent = tasksCompletedPercent,
        targetAmount = Amount(targetAmount, currency),
        totalFundsRaised = Amount(totalFundsRaised, currency),
        analytics = analytics.orEmpty(),
        userID = ID(userID),
        hasRequestedFund = hasRequestedFund,
        isSponsored = isSponsored,
        accountabilityPartners = emptyList()
    )
}

fun ApiPlan.asPlanEntity() = PlanEntity(
    id = id,
    avatar = avatar,
    category = category,
    sector = sector,
    type = type,
    name = name,
    description = description,
    duration = duration.toLong(),
    estimatedSellingPrice = estimatedSellingPrice.toDouble(),
    estimatedCostPrice = estimatedCostPrice.toDouble(),
    fundsRaisedPercent = fundsRaised.orZero(),
    tasksCompletedPercent = tasksCompleted.orZero(),
    targetAmount = targetAmount.toDouble(),
    totalFundsRaised = totalFundsRaised?.toDouble().orZero(),
    currency = currency,
    analytics = analytics,
    userID = user,
    hasRequestedFund = hasRequestedFund ?: false,
    isSponsored = isSponsored ?: false,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun ApiPlan.asPlan(): Plan {
    val currency = Currency(currency)
    return Plan(
        id = ID(id),
        avatar = ImageUrl(avatar),
        category = category,
        sector = sector,
        type = type,
        name = name,
        description = description,
        duration = TaskDuration(duration),
        estimatedSellingPrice = Amount(estimatedSellingPrice.toDouble(), currency),
        estimatedCostPrice = Amount(estimatedCostPrice.toDouble(), currency),
        fundsRaisedPercent = fundsRaised.orZero(),
        tasksCompletedPercent = tasksCompleted.orZero(),
        targetAmount = Amount(targetAmount.toDouble(), currency),
        totalFundsRaised = Amount(totalFundsRaised?.toDouble().orZero(), currency),
        currency = currency,
        analytics = analytics,
        userID = ID(user),
        hasRequestedFund = hasRequestedFund.orFalse(),
        isSponsored = isSponsored.orFalse(),
        accountabilityPartners = accountabilityPartners.map { ID(it) }
    )
}

fun Plan.asRequest() = SavePlanPayload(
    // todo change default avatar
    category = category,
    sector = sector,
    type = type.orEmpty(),
    name = name,
    description = description,
    duration = duration.value,
    estimatedSellingPrice = estimatedSellingPrice.value,
    estimatedCostPrice = estimatedCostPrice.value
)

fun Plan.asEntity() = PlanEntity(
    id = id.value,
    avatar = avatar.value,
    category = category,
    sector = sector,
    type = type,
    name = name,
    description = description,
    duration = duration.value.toLong(),
    estimatedSellingPrice = estimatedSellingPrice.value,
    estimatedCostPrice = estimatedCostPrice.value,
    fundsRaisedPercent = fundsRaisedPercent,
    tasksCompletedPercent = tasksCompletedPercent,
    targetAmount = targetAmount.value,
    totalFundsRaised = totalFundsRaised.value,
    currency = currency.value,
    analytics = analytics,
    userID = userID.value,
    hasRequestedFund = hasRequestedFund,
    isSponsored = isSponsored,
    createdAt = Date.now().value,
    updatedAt = Date.now().value
)

@JvmName("apiPlanToDomain")
fun List<ApiPlan>.toPlans() = this.map { it.asPlanEntity().asPlan() }

fun List<ApiPlan>.toPlanEntities() = this.map { it.asPlanEntity() }

fun List<PlanEntity>.toPlans() = this.map { it.asPlan() }