package com.closedcircuit.closedcircuitapplication.sponsor.di

import com.closedcircuit.closedcircuitapplication.sponsor.presentation.feature.dashboard.SponsorDashboardViewModel
import com.closedcircuit.closedcircuitapplication.sponsor.presentation.feature.makeoffer.MakeOfferViewModel
import org.koin.dsl.module

val viewModelModule = module {
    factory { MakeOfferViewModel(get(), get()) }
    factory { SponsorDashboardViewModel(get(), get()) }
}