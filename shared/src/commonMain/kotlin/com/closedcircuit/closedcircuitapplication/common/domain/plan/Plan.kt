package com.closedcircuit.closedcircuitapplication.common.domain.plan

import com.closedcircuit.closedcircuitapplication.common.domain.fundrequest.FundRequest
import com.closedcircuit.closedcircuitapplication.common.domain.model.Amount
import com.closedcircuit.closedcircuitapplication.common.domain.model.Currency
import com.closedcircuit.closedcircuitapplication.common.domain.model.ID
import com.closedcircuit.closedcircuitapplication.common.domain.model.ImageUrl
import com.closedcircuit.closedcircuitapplication.common.domain.model.TaskDuration
import com.closedcircuit.closedcircuitapplication.common.util.Empty
import com.closedcircuit.closedcircuitapplication.common.util.Zero
import com.closedcircuit.closedcircuitapplication.core.serialization.JavaSerializable
import kotlinx.serialization.Serializable

@Serializable
data class Plan(
    val id: ID,
    val avatar: ImageUrl,
    val category: String,
    val sector: String,
    val type: String?,
    val name: String,
    val description: String,
    val duration: TaskDuration,
    val estimatedSellingPrice: Amount,
    val estimatedCostPrice: Amount,
    val fundsRaisedPercent: Double,
    val tasksCompletedPercent: Double,
    val targetAmount: Amount,
    val totalFundsRaised: Amount,
    val currency: Currency,
    val lastFundRequest: FundRequest?,
    val analytics: String,
    val userID: ID,
    val hasRequestedFund: Boolean,
    val isSponsored: Boolean,
    val accountabilityPartners: List<ID>,
) : JavaSerializable {

    fun hasReceivedFunds(): Boolean {
        return totalFundsRaised.value > Double.Zero
    }

    companion object {
        fun buildPlan(
            id: ID = ID.generateRandomUUID(),
            avatar: ImageUrl = ImageUrl(String.Empty),
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
            currency: Currency = Currency("USD"),
            planAnalytics: String = String.Empty,
            userID: ID = ID.generateRandomUUID(),
            hasRequestedFund: Boolean = false,
            isSponsored: Boolean = false,
            accountabilityPartners: List<ID> = emptyList(),
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
                fundsRaisedPercent = fundsRaised,
                tasksCompletedPercent = tasksCompleted,
                targetAmount = targetAmount,
                currency = currency,
                lastFundRequest = null,
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
