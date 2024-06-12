package com.closedcircuit.closedcircuitapplication.common.di

import com.closedcircuit.closedcircuitapplication.beneficiary.domain.session.SessionRepository
import com.closedcircuit.closedcircuitapplication.core.logger.CustomLogger
import com.closedcircuit.closedcircuitapplication.core.network.DefaultResponseConverter
import de.jensklingenberg.ktorfit.Ktorfit
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.HttpSendPipeline
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.qualifier.named
import org.koin.dsl.module

val noAuthQualifier = named("noAuth")
val authQualifier = named("auth")
val networkModule = module {
    single(authQualifier) { createHttpClient(get()) }
    single(noAuthQualifier) { createHttpClient() }
    single(authQualifier) { createKtorfit(get(authQualifier)) }
    single(noAuthQualifier) { createNoAuthKtorfit(get(noAuthQualifier)) }
}

private fun createKtorfit(client: HttpClient): Ktorfit {
    return Ktorfit.Builder()
        .httpClient(client)
        .baseUrl("https://theclosedcircuit-staging.herokuapp.com/api/")
        .responseConverter(DefaultResponseConverter())
        .build()
}

private fun createNoAuthKtorfit(client: HttpClient): Ktorfit {
    return Ktorfit.Builder()
        .httpClient(client)
        .baseUrl("https://theclosedcircuit-staging.herokuapp.com/api/")
        .responseConverter(DefaultResponseConverter())
        .build()
}


private fun createHttpClient(): HttpClient {
    val client = HttpClient {
        install(ContentNegotiation) {
            json(
                Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                },
            )
        }
        install(Logging) {
            logger = CustomLogger()
            level = LogLevel.ALL
        }
        install(HttpTimeout) {
            val timeout = 30_000L
            requestTimeoutMillis = timeout
            connectTimeoutMillis = timeout
            socketTimeoutMillis = timeout
        }
    }
    return client
}

private fun createHttpClient(sessionRepository: SessionRepository): HttpClient {
    val client = HttpClient {
        install(ContentNegotiation) {
            json(
                Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                },
            )
        }
        install(Logging) {
            logger = CustomLogger()
            level = LogLevel.ALL
        }
        install(HttpTimeout) {
            val timeout = 30_000L
            requestTimeoutMillis = timeout
            connectTimeoutMillis = timeout
            socketTimeoutMillis = timeout
        }
    }

    client.sendPipeline.intercept(HttpSendPipeline.State) {
        context.headers.append("Authorization", "Bearer ${sessionRepository.getToken()}")
    }
    return client
}