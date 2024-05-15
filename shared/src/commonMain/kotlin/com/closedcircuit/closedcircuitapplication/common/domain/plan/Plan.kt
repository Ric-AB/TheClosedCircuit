package com.closedcircuit.closedcircuitapplication.common.domain.plan

import com.closedcircuit.closedcircuitapplication.beneficiary.domain.model.Avatar
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.model.ID
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.model.Amount
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.model.TaskDuration
import com.closedcircuit.closedcircuitapplication.common.util.Empty
import kotlinx.serialization.Serializable

@Serializable
data class Plan(
    val id: ID,
    val avatar: Avatar,
    val category: String,
    val sector: String,
    val type: String?,
    val name: String,
    val description: String,
    val duration: TaskDuration,
    val estimatedSellingPrice: Amount,
    val estimatedCostPrice: Amount,
    val fundsRaised: Double,
    val tasksCompleted: Double,
    val targetAmount: Amount,
    val totalFundsRaised: Amount,
    val analytics: String,
    val userID: ID,
    val hasRequestedFund: Boolean,
    val isSponsored: Boolean,
    val accountabilityPartners: List<String>,
) {

    companion object {
        fun buildPlan(
            id: ID = ID.generateRandomUUID(),
            avatar: Avatar = Avatar(String.Empty),
            category: String = String.Empty,
            sector: String = String.Empty,
            type: String? = String.Empty,
            name: String = String.Empty,
            description: String = String.Empty,
            duration: TaskDuration = TaskDuration(0),
            estimatedSellingPrice: Amount = Amount(0.0),
            estimatedCostPrice: Amount = Amount(0.0),
            fundsRaised: Double = 0.0,
            tasksCompleted: Double = 0.0,
            targetAmount: Amount = Amount(0.0),
            totalFundsRaised: Amount = Amount(0.0),
            planAnalytics: String = String.Empty,
            userID: ID = ID.generateRandomUUID(),
            hasRequestedFund: Boolean = false,
            isSponsored: Boolean = false,
            accountabilityPartners: List<String> = emptyList(),
        ): Plan {
            return Plan(
                id = id,
                avatar = avatar,
                category = category,
                sector = sector,
                type = type,
                name = name,
                description = description,
                duration = duration,
                estimatedSellingPrice = estimatedSellingPrice,
                estimatedCostPrice = estimatedCostPrice,
                fundsRaised = fundsRaised,
                tasksCompleted = tasksCompleted,
                targetAmount = targetAmount,
                totalFundsRaised = totalFundsRaised,
                analytics = planAnalytics,
                userID = userID,
                hasRequestedFund = hasRequestedFund,
                isSponsored = isSponsored,
                accountabilityPartners = accountabilityPartners
            )
        }
    }
}
