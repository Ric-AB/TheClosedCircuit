package com.closedcircuit.closedcircuitapplication.data.budget

import com.closedcircuit.closedcircuitapplication.core.network.ApiResponse
import com.closedcircuit.closedcircuitapplication.core.network.mapOnSuccess
import com.closedcircuit.closedcircuitapplication.database.TheClosedCircuitDatabase
import com.closedcircuit.closedcircuitapplication.domain.budget.Budget
import com.closedcircuit.closedcircuitapplication.domain.budget.BudgetRepository
import com.closedcircuit.closedcircuitapplication.domain.budget.Budgets
import com.closedcircuit.closedcircuitapplication.domain.budget.TempBudget
import com.closedcircuit.closedcircuitapplication.domain.model.ID
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.withContext

class BudgetRepositoryImpl(
    database: TheClosedCircuitDatabase,
    private val budgetService: BudgetService,
    private val ioDispatcher: CoroutineDispatcher,
    defaultDispatcher: CoroutineDispatcher
) : BudgetRepository {

    private val queries = database.budgetEntityQueries
    override val budgetsFlow: Flow<Budgets> = flowOf()

    override suspend fun fetchBudgets(): ApiResponse<Budgets> {
        return withContext(ioDispatcher + NonCancellable) {
            budgetService.fetchBudgets().mapOnSuccess { getBudgetsResponse ->
                val budgetEntities = getBudgetsResponse.budgets.asBudgetEntities()
                queries.transaction {
                    budgetEntities.forEach { budgetEntity ->
                        queries.upsertBudgetEntity(budgetEntity)
                    }
                }

                budgetEntities.asBudgets()
            }
        }
    }

    override suspend fun createBudget(tempBudget: TempBudget): ApiResponse<Budget> {
        return withContext(ioDispatcher + NonCancellable) {
            budgetService.createBudget(tempBudget.asRequest()).mapOnSuccess { apiBudget ->
                val budgetEntity = apiBudget.asBudgetEntity()
                queries.upsertBudgetEntity(budgetEntity)
                budgetEntity.asBudget()
            }
        }
    }

    override suspend fun updateBudget(budget: Budget): ApiResponse<Budget> {
        return withContext(ioDispatcher + NonCancellable) {
            budgetService.updateBudget(id = budget.id.value, request = budget.asRequest())
                .mapOnSuccess { apiBudget ->
                    val budgetEntity = apiBudget.asBudgetEntity()
                    queries.upsertBudgetEntity(budgetEntity)
                    budgetEntity.asBudget()
                }
        }
    }

    override suspend fun deleteBudget(id: ID): ApiResponse<Budget> {
        return withContext(ioDispatcher + NonCancellable) {
            budgetService.deleteBudget(id = id.value).mapOnSuccess {
                val budgetEntity = queries.getBudgetEntityByID(id = id.value).executeAsOne()
                queries.deleteBudgetEntity(id.value)
                budgetEntity.asBudget()
            }
        }
    }
}