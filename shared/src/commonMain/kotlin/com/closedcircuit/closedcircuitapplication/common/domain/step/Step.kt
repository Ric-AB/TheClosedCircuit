package com.closedcircuit.closedcircuitapplication.common.domain.step

import com.closedcircuit.closedcircuitapplication.common.domain.budget.Budget
import com.closedcircuit.closedcircuitapplication.common.domain.model.Amount
import com.closedcircuit.closedcircuitapplication.common.domain.model.Currency
import com.closedcircuit.closedcircuitapplication.common.domain.model.Date
import com.closedcircuit.closedcircuitapplication.common.domain.model.ID
import com.closedcircuit.closedcircuitapplication.common.domain.model.StepStatus
import com.closedcircuit.closedcircuitapplication.common.domain.model.TaskDuration
import com.closedcircuit.closedcircuitapplication.common.util.Empty
import com.closedcircuit.closedcircuitapplication.common.util.Zero
import kotlinx.serialization.Serializable

@Serializable
data class Step(
    val planID: ID,
    val userID: ID,
    val id: ID,
    val name: String,
    val description: String,
    val duration: TaskDuration,
    val targetFunds: Amount,
    val totalFundsRaised: Amount,
    val currency: Currency,
    val budgets: List<Budget> = emptyList(),
    val isSponsored: Boolean,
    val status: StepStatus,
    val createdAt: Date,
    val updatedAt: Date
) {

    val isComplete: Boolean get() = status == StepStatus.COMPLETED
    val hasReceivedFunds: Boolean get() = totalFundsRaised.value > Double.Zero

    companion object {
        fun buildStep(
            id: ID = ID.generateRandomUUID(),
            name: String = String.Empty,
            description: String = String.Empty,
            duration: TaskDuration = TaskDuration(0),
            targetFunds: Amount = Amount(0.0),
            totalFundsRaised: Amount = Amount(0.0),
            currency: Currency = Currency("USD"),
            planID: ID = ID.generateRandomUUID(),
            userID: ID = ID.generateRandomUUID(),
            isSponsored: Boolean = false,
            status: StepStatus = StepStatus.NOT_COMPLETED,
            createdAt: Date = Date.now(),
            updatedAt: Date = Date.now()
        ): Step {
            return Step(
                id = id,
                name = name,
                description = description,
                duration = duration,
                targetFunds = targetFunds,
                totalFundsRaised = totalFundsRaised,
                currency = currency,
                planID = planID,
                userID = userID,
                isSponsored = isSponsored,
                status = status,
                createdAt = createdAt,
                updatedAt = updatedAt
            )
        }
    }
}

