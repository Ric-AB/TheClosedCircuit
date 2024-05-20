package com.closedcircuit.closedcircuitapplication.sponsor.di

import com.closedcircuit.closedcircuitapplication.common.di.namedIODispatcher
import com.closedcircuit.closedcircuitapplication.sponsor.data.offer.OfferRepositoryImpl
import com.closedcircuit.closedcircuitapplication.sponsor.data.plan.PlanRepositoryImpl
import com.closedcircuit.closedcircuitapplication.sponsor.domain.offer.OfferRepository
import com.closedcircuit.closedcircuitapplication.sponsor.domain.plan.PlanRepository
import org.koin.core.qualifier.named
import org.koin.dsl.module

val dataModule = module {
    single<PlanRepository> { PlanRepositoryImpl(get(), get(named(namedIODispatcher))) }
    single<OfferRepository> { OfferRepositoryImpl(get()) }
}