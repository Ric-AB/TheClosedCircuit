package com.closedcircuit.closedcircuitapplication.common.domain.budget

import com.closedcircuit.closedcircuitapplication.common.domain.model.ID
import com.closedcircuit.closedcircuitapplication.core.network.ApiResponse
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow

typealias Budgets = ImmutableList<Budget>

interface BudgetRepository {

    suspend fun fetchBudgets(): ApiResponse<Budgets>

    fun saveLocally(budget: Budget)

    fun saveLocally(budgets: List<Budget>)

    suspend fun createBudget(budget: Budget): ApiResponse<Budget>

    suspend fun updateBudget(budget: Budget): ApiResponse<Budget>

    suspend fun deleteBudget(id: ID): ApiResponse<Budget>

    fun getBudgetsForPlanAsFlow(planID: ID): Flow<Budgets>

    fun getBudgetsForStepAsFlow(stepID: ID): Flow<Budgets>

    suspend fun getBudgetsForStep(stepID: ID): Budgets
}