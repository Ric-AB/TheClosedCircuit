package com.closedcircuit.closedcircuitapplication.data.budget

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.closedcircuit.closedcircuitapplication.core.network.ApiResponse
import com.closedcircuit.closedcircuitapplication.core.network.ApiSuccessResponse
import com.closedcircuit.closedcircuitapplication.core.network.mapOnSuccess
import com.closedcircuit.closedcircuitapplication.database.TheClosedCircuitDatabase
import com.closedcircuit.closedcircuitapplication.domain.budget.Budget
import com.closedcircuit.closedcircuitapplication.domain.budget.BudgetRepository
import com.closedcircuit.closedcircuitapplication.domain.budget.Budgets
import com.closedcircuit.closedcircuitapplication.domain.model.ID
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlin.time.Duration.Companion.seconds

class BudgetRepositoryImpl(
    database: TheClosedCircuitDatabase,
    private val budgetService: BudgetService,
    private val ioDispatcher: CoroutineDispatcher,
    private val defaultDispatcher: CoroutineDispatcher
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

    override suspend fun createBudget(budget: Budget): ApiResponse<Budget> {
        return withContext(ioDispatcher + NonCancellable) {
            val budgetEntity = budget.asEntity()
            queries.upsertBudgetEntity(budgetEntity)
            delay(3.seconds)
            ApiSuccessResponse(budgetEntity.asBudget())
//            budgetService.createBudget(budget.asRequest()).mapOnSuccess { apiBudget ->
//                val budgetEntity = apiBudget.asBudgetEntity()
//                queries.upsertBudgetEntity(budgetEntity)
//                budgetEntity.asBudget()
//            }
        }
    }

    override suspend fun updateBudget(budget: Budget): ApiResponse<Budget> {
        return withContext(ioDispatcher + NonCancellable) {
            val budgetEntity = budget.asEntity()
            queries.upsertBudgetEntity(budgetEntity)
            delay(3.seconds)
            ApiSuccessResponse(budgetEntity.asBudget())
//            budgetService.updateBudget(id = budget.id.value, request = budget.asRequest())
//                .mapOnSuccess { apiBudget ->
//                    val budgetEntity = apiBudget.asBudgetEntity()
//                    queries.upsertBudgetEntity(budgetEntity)
//                    budgetEntity.asBudget()
//                }
        }
    }

    override suspend fun deleteBudget(id: ID): ApiResponse<Budget> {
        return withContext(ioDispatcher + NonCancellable) {
            val budgetEntity = queries.getBudgetEntityByID(id.value).executeAsOne()
            queries.deleteBudgetEntity(id.value)
            delay(3.seconds)
            ApiSuccessResponse(budgetEntity.asBudget())
//            budgetService.deleteBudget(id = id.value).mapOnSuccess {
//                val budgetEntity = queries.getBudgetEntityByID(id = id.value).executeAsOne()
//                queries.deleteBudgetEntity(id.value)
//                budgetEntity.asBudget()
//            }
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
}