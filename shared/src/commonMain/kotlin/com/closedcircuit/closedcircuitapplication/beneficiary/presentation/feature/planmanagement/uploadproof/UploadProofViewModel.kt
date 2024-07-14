package com.closedcircuit.closedcircuitapplication.beneficiary.presentation.feature.planmanagement.uploadproof

import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.closedcircuit.closedcircuitapplication.common.domain.budget.BudgetRepository
import com.closedcircuit.closedcircuitapplication.common.domain.model.ID
import com.closedcircuit.closedcircuitapplication.common.presentation.util.InputField
import kotlinx.coroutines.launch

class UploadProofViewModel(
    private val budgetID: ID,
    private val budgetRepository: BudgetRepository
) : ScreenModel {

    val state = mutableStateOf(UploadProofUiState())


    init {
        fetchBudget()
    }

    fun onEvent(event: UploadProofUiEvent) {
        when (event) {
            is UploadProofUiEvent.ImageAdded -> addImage(event.bytes)
            is UploadProofUiEvent.ImageRemoved -> removeImage(event.index)
            is UploadProofUiEvent.ProofDescriptionChange -> updateProofDescription(event.description)
        }
    }

    private fun removeImage(index: Int) {
        state.value.uploadItems.removeAt(index)
    }

    private fun updateProofDescription(description: String) {
        state.value.descriptionField.onValueChange(description)
    }

    private fun addImage(bytes: ByteArray) {
        val uploadItem = UploadItem(bytes)
        state.value.uploadItems.add(uploadItem)
    }

    private fun fetchBudget() {
        screenModelScope.launch {
            val budget = budgetRepository.getBudgetById(budgetID)
            state.value = state.value.copy(
                titleField = InputField(inputValue = mutableStateOf(budget.name)),
            )
        }
    }
}