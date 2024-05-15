package com.closedcircuit.closedcircuitapplication.sponsor.di

import com.closedcircuit.closedcircuitapplication.common.di.noAuthQualifier
import com.closedcircuit.closedcircuitapplication.sponsor.data.plan.NoAuthPlanService
import de.jensklingenberg.ktorfit.Ktorfit
import org.koin.dsl.module

val networkModule = module {
    single { createNoAuthPlanService(get(noAuthQualifier)) }
}

private fun createNoAuthPlanService(ktorfit: Ktorfit): NoAuthPlanService = ktorfit.create()