package com.closedcircuit.closedcircuitapplication.beneficiary.domain.budget

import com.closedcircuit.closedcircuitapplication.beneficiary.domain.model.Date
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.model.ID
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.model.Amount
import kotlinx.serialization.Serializable

@Serializable
data class Budget(
    val id: ID,
    val planID: ID,
    val stepID: ID,
    val userID: ID,
    val name: String,
    val description: String,
    val cost: Amount,
    val isSponsored: Boolean,
    val fundsRaised: Amount,
    val isCompleted: Boolean,
    val approvers: List<String>,
    val createdAt: Date,
    val updatedAt: Date
) {
    companion object {
        fun buildBudget(
            id: ID = ID.generateRandomUUID(),
            planID: ID = ID.generateRandomUUID(),
            stepID: ID = ID.generateRandomUUID(),
            userID: ID = ID.generateRandomUUID(),
            name: String = "",
            description: String = "",
            cost: Amount = Amount(0.0),
            isSponsored: Boolean = false,
            fundsRaised: Amount = Amount(0.0),
            isCompleted: Boolean = false,
            approvers: List<String> = emptyList(),
            createdAt: Date = Date.now(),
            updatedAt: Date = Date.now()
        ): Budget {
            return Budget(
                id = id,
                planID = planID,
                stepID = stepID,
                userID = userID,
                name = name,
                description = description,
                cost = cost,
                isSponsored = isSponsored,
                fundsRaised = fundsRaised,
                isCompleted = isCompleted,
                approvers = approvers,
                createdAt = createdAt,
                updatedAt = updatedAt
            )
        }
    }
}
