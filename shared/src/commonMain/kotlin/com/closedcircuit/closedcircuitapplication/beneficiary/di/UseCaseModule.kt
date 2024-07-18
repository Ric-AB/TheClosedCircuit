package com.closedcircuit.closedcircuitapplication.beneficiary.di

import com.closedcircuit.closedcircuitapplication.beneficiary.domain.usecase.CreatePlanUseCase
import com.closedcircuit.closedcircuitapplication.common.domain.usecase.IsLoggedInUseCase
import com.closedcircuit.closedcircuitapplication.common.domain.usecase.LoginUseCase
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.usecase.RegisterUseCase
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import org.koin.dsl.module

val useCaseModule = module {
    single { LoginUseCase(get(), get(), get()) }
    single { RegisterUseCase(get(), get()) }
    single {
        IsLoggedInUseCase(
            firebaseAuth = Firebase.auth,
            sessionRepository = get(),
            appSettingsRepository = get()
        )
    }
    single { CreatePlanUseCase(get(), get(), get()) }
}