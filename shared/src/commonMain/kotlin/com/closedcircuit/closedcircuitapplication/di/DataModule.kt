package com.closedcircuit.closedcircuitapplication.di

import com.closedcircuit.closedcircuitapplication.data.datasource.local.appSettingsStore
import com.closedcircuit.closedcircuitapplication.data.app.AppSettingsRepositoryImpl
import com.closedcircuit.closedcircuitapplication.data.user.UserRepositoryImpl
import com.closedcircuit.closedcircuitapplication.domain.app.AppSettingsRepository
import com.closedcircuit.closedcircuitapplication.domain.user.UserRepository
import org.koin.dsl.module


val dataModule = module {
    single<AppSettingsRepository> { AppSettingsRepositoryImpl(appSettingsStore) }
    single<UserRepository> { UserRepositoryImpl(get()) }
}