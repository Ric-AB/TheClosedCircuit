package com.closedcircuit.closedcircuitapplication.common.di

import com.closedcircuit.closedcircuitapplication.common.data.auth.AuthenticationRepositoryImpl
import com.closedcircuit.closedcircuitapplication.common.data.notification.NotificationRepositoryImpl
import com.closedcircuit.closedcircuitapplication.beneficiary.data.payment.PaymentRepositoryImpl
import com.closedcircuit.closedcircuitapplication.common.data.user.UserRepositoryImpl
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.auth.AuthenticationRepository
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.notification.NotificationRepository
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.payment.PaymentRepository
import com.closedcircuit.closedcircuitapplication.common.domain.session.SessionRepository
import com.closedcircuit.closedcircuitapplication.common.domain.user.UserRepository
import com.closedcircuit.closedcircuitapplication.common.data.app.AppSettingsRepositoryImpl
import com.closedcircuit.closedcircuitapplication.common.data.country.CountryRepositoryImpl
import com.closedcircuit.closedcircuitapplication.common.data.session.SessionRepositoryImpl
import com.closedcircuit.closedcircuitapplication.common.domain.app.AppSettingsRepository
import com.closedcircuit.closedcircuitapplication.common.domain.country.CountryRepository
import com.closedcircuit.closedcircuitapplication.core.storage.appSettingsStore
import com.closedcircuit.closedcircuitapplication.core.storage.sessionStore
import com.closedcircuit.closedcircuitapplication.core.storage.userStore
import com.closedcircuit.closedcircuitapplication.common.data.country.countryJsonString
import kotlinx.serialization.json.Json
import org.koin.core.qualifier.named
import org.koin.dsl.module

val dataModule = module {
    single<AppSettingsRepository> { AppSettingsRepositoryImpl(appSettingsStore) }
    single<SessionRepository> { SessionRepositoryImpl(sessionStore) }
    single<CountryRepository> {
        CountryRepositoryImpl(
            countryData = Json.decodeFromString(countryJsonString)
        )
    }

    single<UserRepository> {
        UserRepositoryImpl(
            userStore = userStore,
            userService = get(),
            countryRepository = get(),
            ioDispatcher = get(named(namedIODispatcher))
        )
    }

    single<AuthenticationRepository> { AuthenticationRepositoryImpl(get()) }
    single<NotificationRepository> {
        NotificationRepositoryImpl(
            notificationService = get(),
            ioDispatcher = get(named(namedIODispatcher))
        )
    }

    single<PaymentRepository> {
        PaymentRepositoryImpl(
            paymentService = get(),
            ioDispatcher = get(named(namedIODispatcher))
        )
    }
}