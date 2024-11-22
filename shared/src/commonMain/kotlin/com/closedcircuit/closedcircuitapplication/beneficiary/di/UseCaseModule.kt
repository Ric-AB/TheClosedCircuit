package com.closedcircuit.closedcircuitapplication.beneficiary.di

import com.closedcircuit.closedcircuitapplication.beneficiary.domain.usecase.CreatePlanUseCase
import com.closedcircuit.closedcircuitapplication.common.domain.usecase.IsLoggedInUseCase
import com.closedcircuit.closedcircuitapplication.common.domain.usecase.LoginUseCase
import com.closedcircuit.closedcircuitapplication.common.domain.usecase.LogoutUseCase
import com.closedcircuit.closedcircuitapplication.common.domain.usecase.RegisterWithLoginUseCase
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import org.koin.dsl.module

val useCaseModule = module {
    single { LoginUseCase(get(), get(), get()) }
    single { LogoutUseCase(get(), get(), get(), get(), get(), get()) }
    single { RegisterWithLoginUseCase(get(), get()) }
    single {
        IsLoggedInUseCase(
            firebaseAuth = Firebase.auth,
            sessionRepository = get(),
            appSettingsRepository = get(),
            logoutUseCase = get()
        )
    }
    single { CreatePlanUseCase(get(), get(), get()) }
}