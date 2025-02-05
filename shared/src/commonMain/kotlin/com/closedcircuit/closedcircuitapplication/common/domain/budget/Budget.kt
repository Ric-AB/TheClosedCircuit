package com.closedcircuit.closedcircuitapplication.common.domain.budget

import com.closedcircuit.closedcircuitapplication.common.domain.model.Amount
import com.closedcircuit.closedcircuitapplication.common.domain.model.Currency
import com.closedcircuit.closedcircuitapplication.common.domain.model.Date
import com.closedcircuit.closedcircuitapplication.common.domain.model.File
import com.closedcircuit.closedcircuitapplication.common.domain.model.ID
import com.closedcircuit.closedcircuitapplication.common.util.Zero
import com.closedcircuit.closedcircuitapplication.core.serialization.JavaSerializable
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
    val fundsRaisedPercent: Double,
    val currency: Currency,
    val isCompleted: Boolean,
    val proofs: List<File>,
    val approvers: List<ID>,
    val createdAt: Date,
    val updatedAt: Date
): JavaSerializable {

    val fundsRaised: Amount
        get() {
            return Amount((fundsRaisedPercent.div(100)) * cost.value, currency)
        }

    val hasUploadedProof: Boolean get() = proofs.isNotEmpty()

    fun isApproved(planAccountabilityPartners: List<ID>): Boolean {
        return approvers.isNotEmpty() && approvers.containsAll(planAccountabilityPartners)
    }

    companion object {
        fun buildBudget(
            id: ID = ID.generateRandomUUID(),
            planID: ID = ID.generateRandomUUID(),
            stepID: ID = ID.generateRandomUUID(),
            userID: ID = ID.generateRandomUUID(),
            name: String = "",
            description: String = "",
            cost: Amount = Amount(Double.Zero),
            currency: Currency = Currency("USD"),
            isSponsored: Boolean = false,
            fundsRaised: Double = Double.Zero,
            isCompleted: Boolean = false,
            proofs: List<File> = emptyList(),
            approvers: List<ID> = emptyList(),
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
                fundsRaisedPercent = fundsRaised,
                currency = currency,
                isCompleted = isCompleted,
                proofs = proofs,
                approvers = approvers,
                createdAt = createdAt,
                updatedAt = updatedAt
            )
        }
    }
}
