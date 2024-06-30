package com.closedcircuit.closedcircuitapplication.sponsor.data.budget

import com.closedcircuit.closedcircuitapplication.common.data.util.ClosedCircuitApiEndpoints
import com.closedcircuit.closedcircuitapplication.core.network.ApiResponse
import com.closedcircuit.closedcircuitapplication.sponsor.data.budget.dto.BudgetApprovalRequest
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.Headers
import de.jensklingenberg.ktorfit.http.PATCH
import de.jensklingenberg.ktorfit.http.Path

interface BudgetService {

    @Headers("Content-Type: application/json")
    @PATCH(ClosedCircuitApiEndpoints.APPROVE_BUDGET)
    suspend fun approveBudget(
        @Path("id") id: String,
        @Body request: BudgetApprovalRequest
    ): ApiResponse<Unit>
}