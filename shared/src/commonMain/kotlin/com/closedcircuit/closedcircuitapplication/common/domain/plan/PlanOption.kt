package com.closedcircuit.closedcircuitapplication.beneficiary.domain.plan

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class PlanOption(
    val id: String,
    val value: String
) {

    override fun toString(): String {
        return this.value
    }

    companion object {
        fun getSectors(): ImmutableList<PlanOption> {
            return persistentListOf(
                PlanOption(id = "technology", value = "Technology"),
                PlanOption(id = "agriculture", value = "Agriculture"),
                PlanOption(id = "professional_services", value = "Professional Services"),
                PlanOption(id = "retail", value = "Retail"),
                PlanOption(id = "hair_and_beauty", value = "Hair and Beauty"),
                PlanOption(id = "financial_services", value = "Financial Services"),
                PlanOption(id = "health_care", value = "Health Care"),
                PlanOption(id = "catering", value = "Catering"),
                PlanOption(id = "hospitality", value = "Hospitality"),
                PlanOption(id = "others", value = "Others"),
            )
        }

        fun getCategories(): ImmutableList<PlanOption> {
            return persistentListOf(
                PlanOption(id = "business", value = "Business"),
                PlanOption(id = "project", value = "Project")
            )
        }

        fun getTypes(): ImmutableList<PlanOption> {
            return persistentListOf(
                PlanOption(id = "service", value = "Service"),
                PlanOption(id = "physical_product", value = "Physical Product"),
                PlanOption(id = "others", value = "Others"),
            )
        }
    }
}