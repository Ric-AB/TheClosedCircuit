package com.closedcircuit.closedcircuitapplication.beneficiary.di

import com.closedcircuit.closedcircuitapplication.beneficiary.data.auth.AuthService
import com.closedcircuit.closedcircuitapplication.beneficiary.data.budget.BudgetService
import com.closedcircuit.closedcircuitapplication.beneficiary.data.donation.DonationService
import com.closedcircuit.closedcircuitapplication.beneficiary.data.fundrequest.FundRequestService
import com.closedcircuit.closedcircuitapplication.beneficiary.data.loan.LoanService
import com.closedcircuit.closedcircuitapplication.beneficiary.data.notification.NotificationService
import com.closedcircuit.closedcircuitapplication.beneficiary.data.payment.PaymentService
import com.closedcircuit.closedcircuitapplication.beneficiary.data.plan.PlanService
import com.closedcircuit.closedcircuitapplication.beneficiary.data.step.StepService
import com.closedcircuit.closedcircuitapplication.beneficiary.data.user.UserService
import com.closedcircuit.closedcircuitapplication.common.di.authQualifier
import com.closedcircuit.closedcircuitapplication.common.di.noAuthQualifier
import de.jensklingenberg.ktorfit.Ktorfit
import org.koin.dsl.module


val networkModule = module {
    single { createUserService(get(authQualifier)) }
    single { createAuthService(get(noAuthQualifier)) }
    single { createPlanService(get(authQualifier)) }
    single { createStepService(get(authQualifier)) }
    single { createBudgetService(get(authQualifier)) }
    single { createNotificationService(get(authQualifier)) }
    single { createFundRequestService(get(authQualifier)) }
    single { createPaymentService(get(authQualifier)) }
    single { createLoanService(get(authQualifier)) }
    single { createDonationService(get(authQualifier)) }
}

private fun createUserService(ktorfit: Ktorfit): UserService = ktorfit.create()
private fun createAuthService(ktorfit: Ktorfit): AuthService = ktorfit.create()
private fun createPlanService(ktorfit: Ktorfit): PlanService = ktorfit.create()
private fun createStepService(ktorfit: Ktorfit): StepService = ktorfit.create()
private fun createBudgetService(ktorfit: Ktorfit): BudgetService = ktorfit.create()
private fun createNotificationService(ktorfit: Ktorfit): NotificationService = ktorfit.create()
private fun createFundRequestService(ktorfit: Ktorfit): FundRequestService = ktorfit.create()
private fun createPaymentService(ktorfit: Ktorfit): PaymentService = ktorfit.create()
private fun createLoanService(ktorfit: Ktorfit): LoanService = ktorfit.create()
private fun createDonationService(ktorfit: Ktorfit): DonationService = ktorfit.create()

