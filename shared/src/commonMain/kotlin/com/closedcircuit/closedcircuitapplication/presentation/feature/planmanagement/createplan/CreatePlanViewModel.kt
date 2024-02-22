package com.closedcircuit.closedcircuitapplication.presentation.feature.planmanagement.createplan

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.closedcircuit.closedcircuitapplication.core.network.onError
import com.closedcircuit.closedcircuitapplication.core.network.onSuccess
import com.closedcircuit.closedcircuitapplication.domain.model.Price
import com.closedcircuit.closedcircuitapplication.domain.model.TaskDuration
import com.closedcircuit.closedcircuitapplication.domain.plan.Plan
import com.closedcircuit.closedcircuitapplication.domain.plan.PlanOption
import com.closedcircuit.closedcircuitapplication.domain.usecase.CreatePlanUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.launch

class CreatePlanViewModel(private val createPlanUseCase: CreatePlanUseCase) : ScreenModel {

    var state by mutableStateOf(CreatePlanUIState())

    private val _createPlanResultChannel = Channel<CreatePlanResult>()
    val createPlanResultChannel: ReceiveChannel<CreatePlanResult> = _createPlanResultChannel

    private var lastFocusedField: String? = null

    fun onEvent(event: CreatePlanUiEvent) {
        when (event) {
            is CreatePlanUiEvent.BusinessType -> updateBusinessType(event.businessType)
            is CreatePlanUiEvent.CategoryChange -> updateCategory(event.category)
            is CreatePlanUiEvent.CostPriceChange -> updateCostPrice(event.price)
            is CreatePlanUiEvent.DurationChange -> updateDuration(event.duration)
            is CreatePlanUiEvent.DescriptionChange -> updateDescription(event.description)
            is CreatePlanUiEvent.NameChange -> updateName(event.name)
            is CreatePlanUiEvent.SectorChange -> updateSector(event.sector)
            is CreatePlanUiEvent.SellingPriceChange -> updateSellingPrice(event.price)
            is CreatePlanUiEvent.InputFieldFocusReceived -> updateLastFocusedField(event.fieldName)
            CreatePlanUiEvent.InputFieldFocusLost -> validateLastFocusedField()
            CreatePlanUiEvent.Submit -> attemptPlanCreation()
        }
    }

    private fun attemptPlanCreation() {
        if (areFieldsValid()) {
            state = state.copy(isLoading = true)
            screenModelScope.launch {
                createPlanUseCase(buildPlan())
                    .onSuccess {
                        state = state.copy(isLoading = false)
                        _createPlanResultChannel.send(CreatePlanResult.Success)
                    }.onError { _, message ->
                        state = state.copy(isLoading = false)
                        _createPlanResultChannel.send(CreatePlanResult.Failure(message))
                    }
            }
        }
    }

    private fun buildPlan(): Plan {
        val defaultSectorAndType = "others"
        return Plan.buildPlan(
            category = state.category?.id.orEmpty(),
            sector = state.sector?.id ?: defaultSectorAndType,
            type = state.businessType?.id ?: defaultSectorAndType,
            name = state.nameField.value,
            description = state.descriptionField.value,
            duration = TaskDuration(state.durationField.value.toInt()),
            estimatedSellingPrice = Price(state.estimatedSellingPriceField.value.toDouble()),
            estimatedCostPrice = Price(state.estimatedCostPriceField.value.toDouble())
        )
    }

    private fun updateBusinessType(businessType: PlanOption) {
        state = state.copy(businessType = businessType)
    }

    private fun updateCategory(category: PlanOption) {
        state = state.copy(category = category)
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