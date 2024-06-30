package com.closedcircuit.closedcircuitapplication.sponsor.data.budget

import com.closedcircuit.closedcircuitapplication.common.domain.model.ID
import com.closedcircuit.closedcircuitapplication.core.network.ApiResponse
import com.closedcircuit.closedcircuitapplication.sponsor.data.budget.dto.BudgetApprovalRequest
import com.closedcircuit.closedcircuitapplication.sponsor.domain.budget.BudgetRepository

class BudgetRepositoryImpl(private val budgetService: BudgetService) : BudgetRepository {

    override suspend fun approveBudget(id: ID): ApiResponse<Unit> {
        val request = BudgetApprovalRequest("approved")
        return budgetService.approveBudget(id.value, request)
    }
}