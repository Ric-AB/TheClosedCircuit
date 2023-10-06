package com.closedcircuit.closedcircuitapplication.presentation.feature.planmanagement.editplan

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.closedcircuit.closedcircuitapplication.core.network.onError
import com.closedcircuit.closedcircuitapplication.core.network.onSuccess
import com.closedcircuit.closedcircuitapplication.domain.model.Price
import com.closedcircuit.closedcircuitapplication.domain.model.TaskDuration
import com.closedcircuit.closedcircuitapplication.domain.plan.Plan
import com.closedcircuit.closedcircuitapplication.domain.plan.PlanOption
import com.closedcircuit.closedcircuitapplication.domain.plan.PlanRepository
import com.closedcircuit.closedcircuitapplication.presentation.feature.planmanagement.createplan.CreatePlanResult
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.launch

class EditPlanViewModel(
    val plan: Plan,
    private val planRepository: PlanRepository
) : ScreenModel {

    var state by mutableStateOf(EditPlanUIState.init(plan))

    private val _editPlanResultChannel = Channel<CreatePlanResult>()
    val editPlanResultChannel: ReceiveChannel<CreatePlanResult> = _editPlanResultChannel

    private var lastFocusedField: String? = null

    fun onEvent(event: EditPlanUIEvent) {
        when (event) {
            is EditPlanUIEvent.BusinessType -> updateBusinessType(event.businessType)
            is EditPlanUIEvent.CostPriceChange -> updateCostPrice(event.price)
            is EditPlanUIEvent.DurationChange -> updateDuration(event.duration)
            is EditPlanUIEvent.DescriptionChange -> updateDescription(event.description)
            is EditPlanUIEvent.NameChange -> updateName(event.name)
            is EditPlanUIEvent.SectorChange -> updateSector(event.sector)
            is EditPlanUIEvent.SellingPriceChange -> updateSellingPrice(event.price)
            is EditPlanUIEvent.InputFieldFocusReceived -> updateLastFocusedField(event.fieldName)
            EditPlanUIEvent.InputFieldFocusLost -> validateLastFocusedField()
            EditPlanUIEvent.Submit -> attemptPlanCreation()
        }
    }

    private fun attemptPlanCreation() {
        if (areFieldsValid()) {
            val defaultSectorAndType = "others"
            val updatedPlan = plan.copy(
                sector = state.sector?.id ?: defaultSectorAndType,
                type = state.businessType?.id ?: defaultSectorAndType,
                name = state.nameField.value,
                description = state.descriptionField.value,
                duration = TaskDuration(state.durationField.value.toInt()),
                estimatedSellingPrice = Price(state.estimatedSellingPriceField.value.toDouble()),
                estimatedCostPrice = Price(state.estimatedCostPriceField.value.toDouble())
            )

            state = state.copy(isLoading = true)
            coroutineScope.launch {
                planRepository.updatePlan(updatedPlan).onSuccess {
                    state = state.copy(isLoading = false)
                    _editPlanResultChannel.send(CreatePlanResult.Success)
                }.onError { _, message ->
                    state = state.copy(isLoading = false)
                    _editPlanResultChannel.send(CreatePlanResult.Failure(message))
                }
            }
        }
    }

    private fun updateBusinessType(businessType: PlanOption) {
        state = state.copy(businessType = businessType)
    }

    private fun updateCostPrice(price: String) {
        state.estimatedCostPriceField.onValueChange(price)
    }

    private fun updateDuration(duration: String) {
        state.durationField.onValueChange(duration)
    }

    private fun updateDescription(description: String) {
        state.descriptionField.onValueChange(description)
    }

    private fun updateName(name: String) {
        state.nameField.onValueChange(name)
    }

    private fun updateSector(sector: PlanOption) {
        state = state.copy(sector = sector)
    }

    private fun updateSellingPrice(price: String) {
        state.estimatedSellingPriceField.onValueChange(price)
    }

    private fun areFieldsValid(): Boolean {
        return state.fieldsToValidate.let { fields ->
            fields.forEach { it.validateInput() }
            fields.all { !it.isError }
        }
    }

    private fun validateLastFocusedField() {
        val fieldToValidate = state.fieldsToValidate.find { it.name == lastFocusedField }
        fieldToValidate?.validateInput()
    }

    private fun updateLastFocusedField(fieldName: String) {
        lastFocusedField = fieldName
    }
}