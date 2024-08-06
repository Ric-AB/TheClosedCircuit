package com.closedcircuit.closedcircuitapplication.common.domain.usecase

import com.closedcircuit.closedcircuitapplication.common.domain.budget.BudgetRepository
import com.closedcircuit.closedcircuitapplication.common.domain.fundrequest.FundRequestRepository
import com.closedcircuit.closedcircuitapplication.common.domain.plan.PlanRepository
import com.closedcircuit.closedcircuitapplication.common.domain.session.SessionRepository
import com.closedcircuit.closedcircuitapplication.common.domain.step.StepRepository
import com.closedcircuit.closedcircuitapplication.common.domain.user.UserRepository

class LogoutUseCase(
    private val planRepository: PlanRepository,
    private val stepRepository: StepRepository,
    private val budgetRepository: BudgetRepository,
    private val fundRequestRepository: FundRequestRepository,
    private val userRepository: UserRepository,
    private val sessionRepository: SessionRepository
) {

    suspend operator fun invoke() {
        planRepository.clear()
        stepRepository.clear()
        budgetRepository.clear()
        fundRequestRepository.clear()
        userRepository.clear()
        sessionRepository.clear()
    }
}