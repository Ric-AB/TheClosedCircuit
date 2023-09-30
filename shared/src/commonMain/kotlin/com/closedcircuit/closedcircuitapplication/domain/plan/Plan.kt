package com.closedcircuit.closedcircuitapplication.domain.plan

import com.closedcircuit.closedcircuitapplication.domain.model.Avatar
import com.closedcircuit.closedcircuitapplication.domain.model.ID
import com.closedcircuit.closedcircuitapplication.domain.model.Price
import com.closedcircuit.closedcircuitapplication.domain.model.TaskDuration
import com.closedcircuit.closedcircuitapplication.util.Empty

data class Plan(
    val id: ID,
    val avatar: Avatar,
    val category: String,
    val sector: String,
    val type: String?,
    val name: String,
    val description: String,
    val duration: TaskDuration,
    val estimatedSellingPrice: Price,
    val estimatedCostPrice: Price,
    val fundsRaised: Price,
    val tasksCompleted: Double,
    val targetAmount: Price,
    val totalFundsRaised: Price,
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
            estimatedSellingPrice: Price = Price(0.0),
            estimatedCostPrice: Price = Price(0.0),
            fundsRaised: Price = Price(0.0),
            tasksCompleted: Double = 0.0,
            targetAmount: Price = Price(0.0),
            totalFundsRaised: Price = Price(0.0),
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
