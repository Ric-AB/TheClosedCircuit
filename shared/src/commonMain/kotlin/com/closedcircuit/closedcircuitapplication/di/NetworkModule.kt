package com.closedcircuit.closedcircuitapplication.di

import com.closedcircuit.closedcircuitapplication.data.user.KtorfitUserService
import de.jensklingenberg.ktorfit.Ktorfit
import de.jensklingenberg.ktorfit.ktorfit
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val networkModule = module {
    single { createKtorfit() }
    single { createUserService(get()) }
}

private fun createKtorfit() = ktorfit {
    baseUrl("https://theclosedcircuit-staging.herokuapp.com/api/")
    httpClient(HttpClient {
        install(ContentNegotiation) {
            json(Json { isLenient = true; ignoreUnknownKeys = true })
        }
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.INFO
        }
    })
}

private fun createUserService(ktorfit: Ktorfit): KtorfitUserService = ktorfit.create()