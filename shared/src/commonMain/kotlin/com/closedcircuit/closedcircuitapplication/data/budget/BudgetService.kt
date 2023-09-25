package com.closedcircuit.closedcircuitapplication.data.budget

import com.closedcircuit.closedcircuitapplication.core.network.ApiResponse
import com.closedcircuit.closedcircuitapplication.data.budget.dto.ApiBudget
import com.closedcircuit.closedcircuitapplication.data.budget.dto.CreateOrUpdateBudgetRequest
import com.closedcircuit.closedcircuitapplication.data.budget.dto.GetBudgetsResponse
import com.closedcircuit.closedcircuitapplication.util.ClosedCircuitApiEndpoints.BUDGET
import com.closedcircuit.closedcircuitapplication.util.ClosedCircuitApiEndpoints.BUDGETS
import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.DELETE
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Headers
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.PUT
import de.jensklingenberg.ktorfit.http.Path

interface BudgetService {

    @GET(BUDGETS)
    suspend fun fetchBudgets(): ApiResponse<GetBudgetsResponse>

    @Headers("Content-Type: application/json")
    @POST(BUDGETS)
    suspend fun createBudget(@Body request: CreateOrUpdateBudgetRequest): ApiResponse<ApiBudget>

    @Headers("Content-Type: application/json")
    @PUT(BUDGET)
    suspend fun updateBudget(
        @Path("id") id: String,
        @Body request: CreateOrUpdateBudgetRequest
    ): ApiResponse<ApiBudget>

    @DELETE(BUDGET)
    suspend fun deleteBudget(@Path("id") id: String): ApiResponse<Unit>
}