package com.closedcircuit.closedcircuitapplication.sponsor.di

import com.closedcircuit.closedcircuitapplication.common.di.authQualifier
import com.closedcircuit.closedcircuitapplication.common.di.noAuthQualifier
import com.closedcircuit.closedcircuitapplication.sponsor.data.loan.LoanService
import com.closedcircuit.closedcircuitapplication.sponsor.data.offer.OfferService
import com.closedcircuit.closedcircuitapplication.sponsor.data.plan.NoAuthPlanService
import com.closedcircuit.closedcircuitapplication.sponsor.data.plan.PlanService
import de.jensklingenberg.ktorfit.Ktorfit
import org.koin.dsl.module

val networkModule = module {
    single { createNoAuthPlanService(get(noAuthQualifier)) }
    single { createOfferService(get(authQualifier)) }
    single { createPlanService(get(authQualifier)) }
    single { createLoanService(get(authQualifier)) }
}

private fun createNoAuthPlanService(ktorfit: Ktorfit): NoAuthPlanService = ktorfit.create()
private fun createOfferService(ktorfit: Ktorfit): OfferService = ktorfit.create()
private fun createPlanService(ktorfit: Ktorfit): PlanService = ktorfit.create()
private fun createLoanService(ktorfit: Ktorfit): LoanService = ktorfit.create()