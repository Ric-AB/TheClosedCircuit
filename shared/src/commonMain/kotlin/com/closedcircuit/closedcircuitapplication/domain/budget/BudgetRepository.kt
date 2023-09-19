package com.closedcircuit.closedcircuitapplication.domain.budget

import com.closedcircuit.closedcircuitapplication.core.network.ApiResponse
import com.closedcircuit.closedcircuitapplication.domain.model.ID
import com.closedcircuit.closedcircuitapplication.domain.step.Step
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow

typealias Budgets = ImmutableList<Budget>

interface BudgetRepository {

    val budgetsFlow: Flow<Budgets>

    suspend fun fetchBudgets(): ApiResponse<Budgets>

    suspend fun createBudget(): ApiResponse<Budget>

    suspend fun updateBudget(): ApiResponse<Budget>

    suspend fun deleteBudget(id: ID): ApiResponse<Budget>
}