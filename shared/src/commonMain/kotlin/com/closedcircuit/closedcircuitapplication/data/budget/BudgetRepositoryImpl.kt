package com.closedcircuit.closedcircuitapplication.data.budget

import com.closedcircuit.closedcircuitapplication.core.network.ApiResponse
import com.closedcircuit.closedcircuitapplication.data.step.StepService
import com.closedcircuit.closedcircuitapplication.database.TheClosedCircuitDatabase
import com.closedcircuit.closedcircuitapplication.domain.budget.Budget
import com.closedcircuit.closedcircuitapplication.domain.budget.BudgetRepository
import com.closedcircuit.closedcircuitapplication.domain.budget.Budgets
import com.closedcircuit.closedcircuitapplication.domain.model.ID
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class BudgetRepositoryImpl(
    database: TheClosedCircuitDatabase,
    private val budgetService: BudgetService,
    private val ioDispatcher: CoroutineDispatcher,
    defaultDispatcher: CoroutineDispatcher
) : BudgetRepository {

    private val queries = database.budgetEntityQueries
    override val budgetsFlow: Flow<Budgets> = flowOf()

    override suspend fun fetchBudgets(): ApiResponse<Budgets> {
        TODO("Not yet implemented")
    }

    override suspend fun createBudget(): ApiResponse<Budget> {
        TODO("Not yet implemented")
    }

    override suspend fun updateBudget(): ApiResponse<Budget> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteBudget(id: ID): ApiResponse<Budget> {
        TODO("Not yet implemented")
    }
}