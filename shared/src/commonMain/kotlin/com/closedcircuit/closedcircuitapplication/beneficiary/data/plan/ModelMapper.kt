package com.closedcircuit.closedcircuitapplication.beneficiary.data.plan

import com.closedcircuit.closedcircuitapplication.beneficiary.data.plan.dto.ApiPlan
import com.closedcircuit.closedcircuitapplication.beneficiary.data.plan.dto.SavePlanPayload
import com.closedcircuit.closedcircuitapplication.common.domain.model.Amount
import com.closedcircuit.closedcircuitapplication.common.domain.model.ImageUrl
import com.closedcircuit.closedcircuitapplication.common.domain.model.Date
import com.closedcircuit.closedcircuitapplication.common.domain.model.ID
import com.closedcircuit.closedcircuitapplication.common.domain.model.TaskDuration
import com.closedcircuit.closedcircuitapplication.common.domain.plan.Plan
import com.closedcircuit.closedcircuitapplication.common.util.orFalse
import com.closedcircuit.closedcircuitapplication.common.util.orZero
import database.PlanEntity
import kotlinx.collections.immutable.toImmutableList

fun PlanEntity.asPlan() = Plan(
    id = ID(id),
    avatar = ImageUrl(avatar),
    category = category,
    sector = sector,
    type = type,
    name = name,
    description = description,
    duration = TaskDuration(duration),
    estimatedSellingPrice = Amount(estimatedSellingPrice),
    estimatedCostPrice = Amount(estimatedCostPrice),
    fundsRaised = fundsRaised,
    tasksCompleted = tasksCompleted,
    targetAmount = Amount(targetAmount),
    totalFundsRaised = Amount(totalFundsRaised),
    analytics = analytics.orEmpty(),
    userID = ID(userID),
    hasRequestedFund = hasRequestedFund,
    isSponsored = isSponsored,
    accountabilityPartners = emptyList()
)

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
    fundsRaised = fundsRaised.orZero(),
    tasksCompleted = tasksCompleted.orZero(),
    targetAmount = targetAmount.toDouble(),
    totalFundsRaised = totalFundsRaised?.toDouble().orZero(),
    analytics = analytics,
    userID = user,
    hasRequestedFund = hasRequestedFund ?: false,
    isSponsored = isSponsored ?: false,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun ApiPlan.asPlan() = Plan(
    id = ID(id),
    avatar = ImageUrl(avatar),
    category = category,
    sector = sector,
    type = type,
    name = name,
    description = description,
    duration = TaskDuration(duration),
    estimatedSellingPrice = Amount(estimatedSellingPrice.toDouble()),
    estimatedCostPrice = Amount(estimatedCostPrice.toDouble()),
    fundsRaised = fundsRaised.orZero(),
    tasksCompleted = tasksCompleted.orZero(),
    targetAmount = Amount(targetAmount.toDouble()),
    totalFundsRaised = Amount(totalFundsRaised?.toDouble().orZero()),
    analytics = analytics,
    userID = ID(user),
    hasRequestedFund = hasRequestedFund.orFalse(),
    isSponsored = isSponsored.orFalse(),
    accountabilityPartners = accountabilityPartners.map { ID(it) }
)

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
    fundsRaised = fundsRaised,
    tasksCompleted = tasksCompleted,
    targetAmount = targetAmount.value,
    totalFundsRaised = totalFundsRaised.value,
    analytics = analytics,
    userID = userID.value,
    hasRequestedFund = hasRequestedFund,
    isSponsored = isSponsored,
    createdAt = Date.now().value,
    updatedAt = Date.now().value
)

fun List<ApiPlan>.toPlans() = this.map { it.asPlanEntity().asPlan() }.toImmutableList()

fun List<ApiPlan>.asPlanEntities() = this.map { it.asPlanEntity() }

fun List<PlanEntity>.asPlans() = this.map { it.asPlan() }.toImmutableList()