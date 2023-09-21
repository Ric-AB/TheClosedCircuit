package com.closedcircuit.closedcircuitapplication.domain.step

import com.closedcircuit.closedcircuitapplication.domain.model.Date
import com.closedcircuit.closedcircuitapplication.domain.model.ID
import com.closedcircuit.closedcircuitapplication.domain.model.Price
import com.closedcircuit.closedcircuitapplication.domain.model.TaskDuration
import com.closedcircuit.closedcircuitapplication.util.Empty

data class Step(
    val id: ID,
    val name: String,
    val description: String,
    val duration: TaskDuration,
    val targetFunds: Price,
    val totalFundsRaised: Price,
    val planID: ID,
    val userID: ID,
    val isSponsored: Boolean,
    val status: String,
    val createdAt: Date,
    val updatedAt: Date
) {
    companion object {
        fun buildStep(
            id: ID = ID.generateRandomUUID(),
            name: String = String.Empty,
            description: String = String.Empty,
            duration: TaskDuration = TaskDuration(0),
            targetFunds: Price = Price(0.0),
            totalFundsRaised: Price = Price(0.0),
            planID: ID = ID.generateRandomUUID(),
            userID: ID = ID.generateRandomUUID(),
            isSponsored: Boolean = false,
            status: String = String.Empty,
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

