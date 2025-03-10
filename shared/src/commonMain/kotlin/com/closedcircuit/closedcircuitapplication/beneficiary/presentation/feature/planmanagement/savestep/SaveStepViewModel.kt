package com.closedcircuit.closedcircuitapplication.beneficiary.presentation.feature.planmanagement.savestep

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.closedcircuit.closedcircuitapplication.common.domain.budget.Budget
import com.closedcircuit.closedcircuitapplication.common.domain.budget.BudgetRepository
import com.closedcircuit.closedcircuitapplication.common.domain.budget.Budgets
import com.closedcircuit.closedcircuitapplication.common.domain.model.Amount
import com.closedcircuit.closedcircuitapplication.common.domain.model.ID
import com.closedcircuit.closedcircuitapplication.common.domain.model.TaskDuration
import com.closedcircuit.closedcircuitapplication.common.domain.step.Step
import com.closedcircuit.closedcircuitapplication.common.domain.step.StepRepository
import com.closedcircuit.closedcircuitapplication.core.network.ApiErrorResponse
import com.closedcircuit.closedcircuitapplication.core.network.ApiSuccessResponse
import com.closedcircuit.closedcircuitapplication.core.network.onError
import com.closedcircuit.closedcircuitapplication.core.network.onSuccess
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.launch

class SaveStepViewModel(
    private val planID: ID,
    private val step: Step?,
    private val stepRepository: StepRepository,
    private val budgetRepository: BudgetRepository
) : ScreenModel {

    var state by mutableStateOf(SaveStepUIState.init(step))

    private var lastFocusedField: String? = null
    private val budgetsToDelete = mutableListOf<Budget>()

    private val _saveStepResult = Channel<SaveStepResult>()
    val saveStepResult: ReceiveChannel<SaveStepResult> = _saveStepResult

    init {
        getBudgetsForStep()
    }


    fun onEvent(event: SaveStepUiEvent) {
        when (event) {
            is SaveStepUiEvent.DescriptionChange -> updateDescription(event.description)
            is SaveStepUiEvent.DurationChange -> updateDuration(event.duration)
            is SaveStepUiEvent.StepNameChange -> updateName(event.name)
            is SaveStepUiEvent.BudgetNameChange -> updateBudgetName(event.name)
            is SaveStepUiEvent.BudgetCostChange -> updateBudgetCost(event.cost)
            is SaveStepUiEvent.EditBudgetItem -> setCurrentBudgetItem(event.index)
            is SaveStepUiEvent.RemoveBudgetItem -> removeBudgetItem(event.index)
            SaveStepUiEvent.InitializeBudgetItem -> initializeNewBudgetItem()
            SaveStepUiEvent.ClearCurrentBudgetItem -> clearCurrentBudgetItem()
            is SaveStepUiEvent.InputFieldFocusReceived -> updateLastFocusedField(event.fieldName)
            SaveStepUiEvent.InputFieldFocusLost -> validateLastFocusedField()
            SaveStepUiEvent.SubmitBudgetItem -> submitCurrentBudgetItem()
            SaveStepUiEvent.SubmitAll -> submitStepAndBudgets()
        }
    }

    private fun getBudgetsForStep() {
        if (step != null) {
            screenModelScope.launch {
                val budgets = budgetRepository.getBudgetsForStep(step.id)
                setBudgetItemsInState(budgets)
            }
        }
    }

    private fun submitStepAndBudgets() {
        if (!areStepFieldsValid()) return
        upsertStepAndBudgets()
    }

    private fun upsertStepAndBudgets() {
        state = state.copy(loading = true)

        val (stepAction, step) = buildStep()
        screenModelScope.launch {
//            val postResults = buildBudgets(step.id).map {
//                val (budgetAction, budget) = it
//                when (budgetAction) {
//                    Action.CREATE -> budgetRepository.createBudget(budget)
//                    Action.UPDATE -> budgetRepository.updateBudget(budget)
//                }
//            }
//
//            val deleteResults = budgetsToDelete.map { budget ->
//                budgetRepository.deleteBudget(budget.id)
//            }
//
//            val allResults = postResults + deleteResults
//            val completedSuccessfully = allResults.all { it is ApiSuccessResponse }
//            if (completedSuccessfully) {
//                state = state.copy(loading = false)
//                _saveStepResult.send(SaveStepResult.Success)
//            } else {
//                state = state.copy(loading = false)
//                val errorResult = allResults.find { it is ApiErrorResponse }
//                _saveStepResult.send(SaveStepResult.Failure((errorResult as ApiErrorResponse).errorMessage))
//            }

            //
            val stepResult = if (stepAction == Action.CREATE) stepRepository.createStep(step)
            else stepRepository.updateStep(step)

            stepResult.onSuccess { step ->
                val postResults = buildBudgets(step.id).map {
                    val (budgetAction, budget) = it
                    when (budgetAction) {
                        Action.CREATE -> budgetRepository.createBudget(budget)
                        Action.UPDATE -> budgetRepository.updateBudget(budget)
                    }
                }

                val deleteResults = budgetsToDelete.map { budget ->
                    budgetRepository.deleteBudget(budget.id)
                }

                val allResults = postResults + deleteResults

                val completedSuccessfully = allResults.all { it is ApiSuccessResponse }
                if (completedSuccessfully) {
                    state = state.copy(loading = false)
                    _saveStepResult.send(SaveStepResult.Success)
                } else {
                    state = state.copy(loading = false)
                    val errorResult = allResults.find { it is ApiErrorResponse }
                    _saveStepResult.send(SaveStepResult.Failure((errorResult as ApiErrorResponse).errorMessage))
                }

            }.onError { _, message ->
                state = state.copy(loading = false)
                _saveStepResult.send(SaveStepResult.Failure(message))
            }
        }
    }

    private fun buildStep(): Pair<Action, Step> {
        val stepName = state.stepNameField.value
        val stepDescription = state.stepDescriptionField.value
        val stepDuration = state.stepDurationField.value.toInt()

        return if (step == null) {
            val step = Step.buildStep(
                name = stepName,
                description = stepDescription,
                duration = TaskDuration(stepDuration),
                planID = planID
            )
            Pair(Action.CREATE, step)
        } else {
            val step = step.copy(
                name = stepName,
                description = stepDescription,
                duration = TaskDuration(stepDuration)
            )
            Pair(Action.UPDATE, step)
        }
    }

    private fun buildBudgets(stepID: ID): List<Pair<Action, Budget>> {
        return state.budgetItemsState.map {
            val budgetName = it.budgetNameField.value
            val budgetCost = it.budgetCostField.value.toDouble()

            if (it.budget == null) {
                val budget = Budget.buildBudget(
                    planID = planID,
                    stepID = stepID,
                    name = budgetName,
                    description = budgetName,
                    cost = Amount(budgetCost)
                )
                Pair(Action.CREATE, budget)
            } else {
                val budget = it.budget.copy(
                    name = budgetName,
                    description = budgetName,
                    cost = Amount(budgetCost)
                )
                Pair(Action.UPDATE, budget)
            }
        }
    }

    private fun updateDescription(description: String) {
        state.stepDescriptionField.onValueChange(description)
    }

    private fun updateDuration(duration: String) {
        state.stepDurationField.onValueChange(duration)
    }

    private fun updateName(name: String) {
        state.stepNameField.onValueChange(name)
    }

    private fun initializeNewBudgetItem() {
        state = state.copy(currentBudgetItem = BudgetItemState())
    }

    private fun setCurrentBudgetItem(index: Int) {
        val currentBudgetItem = state.budgetItemsState[index].copy(indexOfItem = index)
        state = state.copy(currentBudgetItem = currentBudgetItem)
    }

    private fun updateBudgetName(name: String) {
        state.currentBudgetItem?.budgetNameField?.onValueChange(name)
    }

    private fun updateBudgetCost(cost: String) {
        state.currentBudgetItem?.budgetCostField?.onValueChange(cost)
    }

    private fun removeBudgetItem(index: Int) {
        val itemToRemove = state.budgetItemsState[index]
        state.budgetItemsState.remove(itemToRemove)

        if (itemToRemove.budget != null)
            budgetsToDelete.add(itemToRemove.budget)
    }

    private fun clearCurrentBudgetItem() {
        state = state.copy(currentBudgetItem = null)
    }

    private fun submitCurrentBudgetItem() {
        if (!areBudgetFieldsValid()) return

        val currentBudgetItem = state.currentBudgetItem
        when {
            currentBudgetItem != null && currentBudgetItem.indexOfItem < 0 ->
                addBudgetItemToList(currentBudgetItem)

            currentBudgetItem != null && currentBudgetItem.indexOfItem >= 0 ->
                replaceBudgetItem(currentBudgetItem)
        }

        state = state.copy(currentBudgetItem = null)
    }

    private fun addBudgetItemToList(newItem: BudgetItemState) {
        state.budgetItemsState.add(newItem)
    }

    private fun replaceBudgetItem(item: BudgetItemState) {
        state.budgetItemsState[item.indexOfItem] = item
    }

    private fun setBudgetItemsInState(items: Budgets) {
        items.forEach { state.budgetItemsState.add(BudgetItemState.init(it)) }
    }

    private fun updateLastFocusedField(fieldName: String) {
        lastFocusedField = fieldName
    }

    private fun validateLastFocusedField() {
        val field = state.fieldsToValidate.find { it.name == lastFocusedField }
        field?.validateInput()
    }

    private fun areStepFieldsValid(): Boolean {
        val fields = state.fieldsToValidate
        return fields.let { receivedFields ->
            receivedFields.forEach { it.validateInput() }
            receivedFields.all { !it.isError }
        }
    }

    private fun areBudgetFieldsValid(): Boolean {
        val fields = state.currentBudgetItem?.fieldsToValidate ?: return false
        return fields.let { receivedFields ->
            receivedFields.forEach { it.validateInput() }
            receivedFields.all { !it.isError }
        }
    }

    private enum class Action {
        CREATE, UPDATE
    }
}