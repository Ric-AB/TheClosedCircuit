package com.closedcircuit.closedcircuitapplication.beneficiary.data.budget

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.closedcircuit.closedcircuitapplication.common.domain.budget.Budget
import com.closedcircuit.closedcircuitapplication.common.domain.budget.BudgetRepository
import com.closedcircuit.closedcircuitapplication.common.domain.budget.Budgets
import com.closedcircuit.closedcircuitapplication.common.domain.model.ID
import com.closedcircuit.closedcircuitapplication.core.network.ApiResponse
import com.closedcircuit.closedcircuitapplication.core.network.mapOnSuccess
import com.closedcircuit.closedcircuitapplication.database.TheClosedCircuitDatabase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class BudgetRepositoryImpl(
    database: TheClosedCircuitDatabase,
    private val budgetService: BudgetService,
    private val ioDispatcher: CoroutineDispatcher,
    private val defaultDispatcher: CoroutineDispatcher
) : BudgetRepository {

    private val queries = database.budgetEntityQueries

    // todo use save locally
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

    override fun saveLocally(budget: Budget) {
        queries.upsertBudgetEntity(budget.asEntity())
    }

    override fun saveLocally(budgets: List<Budget>) {
        queries.transaction {
            budgets.forEach {
                queries.upsertBudgetEntity(it.asEntity())
            }
        }
    }

    override suspend fun createBudget(budget: Budget): ApiResponse<Budget> {
        return withContext(ioDispatcher + NonCancellable) {
            budgetService.createBudget(budget.asRequest()).mapOnSuccess { apiBudget ->
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

    override fun getBudgetsForPlanAsFlow(planID: ID): Flow<Budgets> {
        return queries.getBudgetEntitiesForPlan(planID.value)
            .asFlow()
            .mapToList(defaultDispatcher)
            .map { it.asBudgets() }
    }

    override fun getBudgetsForStepAsFlow(stepID: ID): Flow<Budgets> {
        return queries.getBudgetEntitiesForStep(stepID.value)
            .asFlow()
            .mapToList(defaultDispatcher)
            .map { it.asBudgets() }
    }

    override suspend fun getBudgetsForStep(stepID: ID): Budgets {
        return queries.getBudgetEntitiesForStep(stepID.value)
            .executeAsList()
            .asBudgets()
    }

    override suspend fun getBudgetById(id: ID): Budget {
        return queries.getBudgetEntityByID(id.value).executeAsOne().asBudget()
    }
}