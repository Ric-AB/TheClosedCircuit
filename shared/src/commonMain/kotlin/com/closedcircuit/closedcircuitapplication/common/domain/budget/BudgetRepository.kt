package com.closedcircuit.closedcircuitapplication.common.domain.budget

import com.closedcircuit.closedcircuitapplication.common.domain.model.File
import com.closedcircuit.closedcircuitapplication.common.domain.model.ID
import com.closedcircuit.closedcircuitapplication.core.network.ApiResponse
import kotlinx.coroutines.flow.Flow

typealias Budgets = List<Budget>

interface BudgetRepository {

    suspend fun fetchBudgets(): ApiResponse<Budgets>

    fun saveBudgetLocally(budgets: List<Budget>)

    suspend fun createBudget(budget: Budget): ApiResponse<Budget>

    suspend fun updateBudget(budget: Budget): ApiResponse<Budget>

    suspend fun deleteBudget(id: ID): ApiResponse<Budget>

    fun deleteBudgetsLocally(budgets: Budgets)

    fun getBudgetsForPlanAsFlow(planID: ID): Flow<Budgets>

    fun getBudgetsForStepAsFlow(stepID: ID): Flow<Budgets>

    suspend fun getBudgetsForStep(stepID: ID): Budgets

    suspend fun getBudgetById(id: ID): Budget

    suspend fun fetchBudgetById(id: ID): ApiResponse<Budget>

    suspend fun uploadProof(budgetID: ID, files: List<File>): ApiResponse<Unit>

    suspend fun clear()
}