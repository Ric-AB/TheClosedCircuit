package com.closedcircuit.closedcircuitapplication.presentation.feature.planmanagement.editplan

import androidx.compose.runtime.mutableStateOf
import com.closedcircuit.closedcircuitapplication.domain.plan.Plan
import com.closedcircuit.closedcircuitapplication.domain.plan.PlanOption
import com.closedcircuit.closedcircuitapplication.util.InputField
import com.closedcircuit.closedcircuitapplication.util.validation.AmountValidator
import com.closedcircuit.closedcircuitapplication.util.validation.TextValidator
import kotlinx.collections.immutable.ImmutableList

data class EditPlanUIState(
    val isLoading: Boolean = false,
    val sector: PlanOption? = null,
    val businessType: PlanOption? = null,
    val nameField: InputField,
    val descriptionField: InputField = InputField(
        name = "planDescription",
        validator = TextValidator()
    ),
    val durationField: InputField = InputField(name = "planDuration", validator = TextValidator()),
    val estimatedSellingPriceField: InputField = InputField(
        name = "sellingPrice",
        validator = AmountValidator()
    ),
    val estimatedCostPriceField: InputField = InputField(
        name = "costPrice",
        validator = AmountValidator()
    ),
    val sectors: ImmutableList<PlanOption> = PlanOption.getSectors(),
    val businessTypes: ImmutableList<PlanOption> = PlanOption.getTypes()
) {
    val fieldsToValidate: List<InputField> = listOf(
        nameField,
        descriptionField,
        durationField,
        estimatedSellingPriceField,
        estimatedCostPriceField
    )

    companion object {
        fun init(plan: Plan): EditPlanUIState {
            return EditPlanUIState(
                sector = PlanOption.getSectors()
                    .find { it.value == plan.sector || it.id == plan.sector },
                businessType = PlanOption.getTypes()
                    .find { it.value == plan.type || it.id == plan.type },
                nameField = InputField(
                    name = "planName",
                    validator = TextValidator(),
                    inputValue = mutableStateOf(plan.name)
                ),
                descriptionField = InputField(
                    name = "planDescription",
                    validator = TextValidator(),
                    inputValue = mutableStateOf(plan.description)
                ),
                durationField = InputField(
                    name = "planDuration",
                    validator = TextValidator(),
                    inputValue = mutableStateOf(plan.duration.value.toString())
                ),
                estimatedSellingPriceField = InputField(
                    name = "sellingPrice",
                    validator = AmountValidator(),
                    inputValue = mutableStateOf(plan.estimatedSellingPrice.value.toInt().toString())
                ),
                estimatedCostPriceField = InputField(
                    name = "costPrice",
                    validator = AmountValidator(),
                    inputValue = mutableStateOf(plan.estimatedCostPrice.value.toInt().toString())
                )
            )
        }
    }
}

sealed interface EditPlanResult {
    object Success : EditPlanResult
    data class Failure(val message: String) : EditPlanResult
}