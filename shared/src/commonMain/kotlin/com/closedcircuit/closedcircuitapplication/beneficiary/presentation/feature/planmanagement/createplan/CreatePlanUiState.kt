package com.closedcircuit.closedcircuitapplication.beneficiary.presentation.feature.planmanagement.createplan

import com.closedcircuit.closedcircuitapplication.common.domain.plan.PlanOption
import com.closedcircuit.closedcircuitapplication.common.presentation.util.InputField
import com.closedcircuit.closedcircuitapplication.common.domain.util.validation.AmountValidator
import com.closedcircuit.closedcircuitapplication.common.domain.util.validation.TextValidator
import kotlinx.collections.immutable.ImmutableList

data class CreatePlanUIState(
    val isLoading: Boolean = false,
    val planImageBytes: ByteArray? = null,
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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as CreatePlanUIState

        if (isLoading != other.isLoading) return false
        if (planImageBytes != null) {
            if (other.planImageBytes == null) return false
            if (!planImageBytes.contentEquals(other.planImageBytes)) return false
        } else if (other.planImageBytes != null) return false
        if (category != other.category) return false
        if (sector != other.sector) return false
        if (businessType != other.businessType) return false
        if (nameField != other.nameField) return false
        if (descriptionField != other.descriptionField) return false
        if (durationField != other.durationField) return false
        if (estimatedSellingPriceField != other.estimatedSellingPriceField) return false
        if (estimatedCostPriceField != other.estimatedCostPriceField) return false
        if (categories != other.categories) return false
        if (sectors != other.sectors) return false
        if (businessTypes != other.businessTypes) return false
        if (fieldsToValidate != other.fieldsToValidate) return false

        return true
    }

    override fun hashCode(): Int {
        var result = isLoading.hashCode()
        result = 31 * result + (planImageBytes?.contentHashCode() ?: 0)
        result = 31 * result + (category?.hashCode() ?: 0)
        result = 31 * result + (sector?.hashCode() ?: 0)
        result = 31 * result + (businessType?.hashCode() ?: 0)
        result = 31 * result + nameField.hashCode()
        result = 31 * result + descriptionField.hashCode()
        result = 31 * result + durationField.hashCode()
        result = 31 * result + estimatedSellingPriceField.hashCode()
        result = 31 * result + estimatedCostPriceField.hashCode()
        result = 31 * result + categories.hashCode()
        result = 31 * result + sectors.hashCode()
        result = 31 * result + businessTypes.hashCode()
        result = 31 * result + fieldsToValidate.hashCode()
        return result
    }
}

sealed interface CreatePlanResult {
    object Success : CreatePlanResult
    data class Failure(val message: String) : CreatePlanResult
}
