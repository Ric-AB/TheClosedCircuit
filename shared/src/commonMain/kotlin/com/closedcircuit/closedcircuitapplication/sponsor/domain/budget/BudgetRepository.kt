package com.closedcircuit.closedcircuitapplication.sponsor.domain.budget

import com.closedcircuit.closedcircuitapplication.common.domain.model.ID
import com.closedcircuit.closedcircuitapplication.core.network.ApiResponse

interface BudgetRepository {

    suspend fun approveBudget(id: ID): ApiResponse<Unit>
}