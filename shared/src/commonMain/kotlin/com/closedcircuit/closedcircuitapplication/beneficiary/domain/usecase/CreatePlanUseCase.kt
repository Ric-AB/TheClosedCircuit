package com.closedcircuit.closedcircuitapplication.beneficiary.domain.usecase

import com.closedcircuit.closedcircuitapplication.core.network.ApiResponse
import com.closedcircuit.closedcircuitapplication.core.network.onSuccess
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.budget.Budget
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.budget.BudgetRepository
import com.closedcircuit.closedcircuitapplication.common.domain.plan.Plan
import com.closedcircuit.closedcircuitapplication.common.domain.plan.PlanRepository
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.step.Step
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.step.StepRepository

class CreatePlanUseCase(
    private val planRepository: PlanRepository,
    private val stepRepository: StepRepository,
    private val budgetRepository: BudgetRepository
) {

    suspend operator fun invoke(plan: Plan): ApiResponse<Plan> {
        return if (plan.category.equals("business", ignoreCase = true)) { //TODO delegate to backend
            planRepository.createPlan(plan).onSuccess { createdPlan ->
                val planID = createdPlan.id

                stepsWithBudgets.forEach { (stepName, budgetNames) ->
                    val step = Step.buildStep(
                        name = stepName,
                        description = stepName,
                        planID = planID
                    )

                    stepRepository.createStep(step).onSuccess { createdStep ->
                        val stepID = createdStep.id

                        budgetNames.forEach {
                            val budget = Budget.buildBudget(
                                name = it,
                                description = it,
                                stepID = stepID,
                                planID = planID
                            )
                            budgetRepository.createBudget(budget)
                        }
                    }
                }
            }
        } else {
            planRepository.createPlan(plan)
        }
    }
}

val stepsWithBudgets: Map<String, List<String>> = mapOf(
    "Conduct Market Research" to listOf("Cost of access to Data"),
    "Register the business" to listOf(
        "Company documentation",
        "Obtain licences and permits",
        "Legal consultation fees"
    ),
    "Create a website and social media pages" to listOf(
        "Website hosting fee",
        "Purchase Domain",
        "Design of the Website",
        "Setup email addresses"
    ),
    "Purchase supplies and equipment" to listOf("Cost of supplies", "Insurance fee for supplies"),
    "Setup accounting and inventory system" to listOf("Accounting services", "Inventory software"),
    "Advertising and Marketing" to listOf(
        "Cost of targeted online adverts",
        "Cost of SEO and analytics",
        "Printed materials"
    ),
    "Obtain physical space" to listOf("Lease office space"),
    "Employ staff" to listOf("Salaries")
)