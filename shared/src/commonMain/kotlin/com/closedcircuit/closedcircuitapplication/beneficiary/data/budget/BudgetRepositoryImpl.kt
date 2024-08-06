package com.closedcircuit.closedcircuitapplication.beneficiary.data.budget

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.closedcircuit.closedcircuitapplication.beneficiary.data.budget.dto.UploadProofRequest
import com.closedcircuit.closedcircuitapplication.common.domain.budget.Budget
import com.closedcircuit.closedcircuitapplication.common.domain.budget.BudgetRepository
import com.closedcircuit.closedcircuitapplication.common.domain.budget.Budgets
import com.closedcircuit.closedcircuitapplication.common.domain.model.File
import com.closedcircuit.closedcircuitapplication.common.domain.model.ID
import com.closedcircuit.closedcircuitapplication.core.network.ApiResponse
import com.closedcircuit.closedcircuitapplication.core.network.mapOnSuccess
import com.closedcircuit.closedcircuitapplication.database.TheClosedCircuitDatabase
import database.BudgetEntity
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
                val budgetEntities = getBudgetsResponse.budgets.toBudgetEntities()
                saveLocally(budgetEntities)
                budgetEntities.toBudgets()
            }
        }
    }

    override fun saveBudgetLocally(budgets: List<Budget>) {
        queries.transaction {
            budgets.forEach {
                saveLocally(it.asEntity())
            }
        }
    }

    override suspend fun createBudget(budget: Budget): ApiResponse<Budget> {
        return withContext(ioDispatcher + NonCancellable) {
            budgetService.createBudget(budget.asRequest()).mapOnSuccess { apiBudget ->
                val budgetEntity = apiBudget.asBudgetEntity()
                saveLocally(budgetEntity)
                budgetEntity.asBudget()
            }
        }
    }

    override suspend fun updateBudget(budget: Budget): ApiResponse<Budget> {
        return withContext(ioDispatcher + NonCancellable) {
            budgetService.updateBudget(id = budget.id.value, request = budget.asRequest())
                .mapOnSuccess { apiBudget ->
                    val budgetEntity = apiBudget.asBudgetEntity()
                    saveLocally(budgetEntity)
                    budgetEntity.asBudget()
                }
        }
    }

    override suspend fun deleteBudget(id: ID): ApiResponse<Budget> {
        return withContext(ioDispatcher + NonCancellable) {
            budgetService.deleteBudget(id = id.value).mapOnSuccess {
                val budgetEntity = queries.getBudgetEntityByID(id = id.value).executeAsOne()
                deleteLocally(budgetEntity.id)
                budgetEntity.asBudget()
            }
        }
    }

    override fun deleteBudgetsLocally(budgets: Budgets) {
        queries.transaction {
            budgets.forEach { deleteLocally(it.id.value) }
        }
    }

    override fun getBudgetsForPlanAsFlow(planID: ID): Flow<Budgets> {
        return queries.getBudgetEntitiesForPlan(planID.value)
            .asFlow()
            .mapToList(defaultDispatcher)
            .map { it.toBudgets() }
    }

    override fun getBudgetsForStepAsFlow(stepID: ID): Flow<Budgets> {
        return queries.getBudgetEntitiesForStep(stepID.value)
            .asFlow()
            .mapToList(defaultDispatcher)
            .map { it.toBudgets() }
    }

    override suspend fun getBudgetsForStep(stepID: ID): Budgets {
        return queries.getBudgetEntitiesForStep(stepID.value)
            .executeAsList()
            .toBudgets()
    }

    override suspend fun getBudgetById(id: ID): Budget {
        return queries.getBudgetEntityByID(id.value).executeAsOne().asBudget()
    }

    override suspend fun fetchBudgetById(id: ID): ApiResponse<Budget> {
        return budgetService.fetchBudgetById(id.value).mapOnSuccess { it.asBudget() }
    }

    override suspend fun uploadProof(budgetID: ID, files: List<File>): ApiResponse<Unit> {
        val request = UploadProofRequest(proof = files)
        return budgetService.uploadProof(id = budgetID.value, request = request)
    }

    override suspend fun clear() {
        queries.deleteAll()
    }

    private fun deleteLocally(id: String) {
        queries.deleteBudgetEntity(id)
    }

    private fun saveLocally(budgetEntity: BudgetEntity) {
        queries.upsertBudgetEntity(budgetEntity)
    }

    private fun saveLocally(budgetEntities: List<BudgetEntity>) {
        queries.transaction {
            budgetEntities.forEach { budgetEntity ->
                saveLocally(budgetEntity)
            }
        }
    }
}