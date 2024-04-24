package com.closedcircuit.closedcircuitapplication.beneficiary.presentation.feature.planmanagement.createplan

import com.closedcircuit.closedcircuitapplication.beneficiary.domain.plan.PlanOption
import com.closedcircuit.closedcircuitapplication.common.util.InputField
import com.closedcircuit.closedcircuitapplication.common.util.validation.AmountValidator
import com.closedcircuit.closedcircuitapplication.common.util.validation.TextValidator
import kotlinx.collections.immutable.ImmutableList

data class CreatePlanUIState(
    val isLoading: Boolean = false,
    val category: PlanOption? = null,
    val sector: PlanOption? = null,
    val businessType: PlanOption? = null,
    val nameField: InputField = InputField(name = "planName", validator = TextValidator()),
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
    val categories: ImmutableList<PlanOption> = PlanOption.getCategories(),
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
}

sealed interface CreatePlanResult {
    object Success : CreatePlanResult
    data class Failure(val message: String) : CreatePlanResult
}
