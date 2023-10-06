package com.closedcircuit.closedcircuitapplication.domain.budget

import com.closedcircuit.closedcircuitapplication.core.network.ApiResponse
import com.closedcircuit.closedcircuitapplication.domain.model.ID
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow

typealias Budgets = ImmutableList<Budget>

interface BudgetRepository {

    val budgetsFlow: Flow<Budgets>

    suspend fun fetchBudgets(): ApiResponse<Budgets>

    suspend fun createBudget(budget: Budget): ApiResponse<Budget>

    suspend fun updateBudget(budget: Budget): ApiResponse<Budget>

    suspend fun deleteBudget(id: ID): ApiResponse<Budget>

    fun getBudgetsForPlanAsFlow(planID: ID): Flow<Budgets>

    fun getBudgetsForStepAsFlow(stepID: ID): Flow<Budgets>

    suspend fun getBudgetsForStep(stepID: ID): Budgets
}