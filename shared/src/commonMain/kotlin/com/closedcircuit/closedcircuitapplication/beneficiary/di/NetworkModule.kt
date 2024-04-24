package com.closedcircuit.closedcircuitapplication.beneficiary.di

import com.closedcircuit.closedcircuitapplication.core.logger.CustomLogger
import com.closedcircuit.closedcircuitapplication.core.network.DefaultResponseConverter
import com.closedcircuit.closedcircuitapplication.beneficiary.data.auth.AuthService
import com.closedcircuit.closedcircuitapplication.beneficiary.data.budget.BudgetService
import com.closedcircuit.closedcircuitapplication.beneficiary.data.donation.DonationService
import com.closedcircuit.closedcircuitapplication.beneficiary.data.fundrequest.FundRequestService
import com.closedcircuit.closedcircuitapplication.beneficiary.data.loan.LoanService
import com.closedcircuit.closedcircuitapplication.beneficiary.data.notification.NotificationService
import com.closedcircuit.closedcircuitapplication.beneficiary.data.payment.PaymentService
import com.closedcircuit.closedcircuitapplication.beneficiary.data.plan.PlanService
import com.closedcircuit.closedcircuitapplication.beneficiary.data.step.StepService
import com.closedcircuit.closedcircuitapplication.beneficiary.data.user.UserService
import com.closedcircuit.closedcircuitapplication.beneficiary.domain.session.SessionRepository
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
    single { createUserService(get(authQualifier)) }
    single { createAuthService(get(noAuthQualifier)) }
    single { createPlanService(get(authQualifier)) }
    single { createStepService(get(authQualifier)) }
    single { createBudgetService(get(authQualifier)) }
    single { createNotificationService(get(authQualifier)) }
    single { createFundRequestService(get(authQualifier)) }
    single { createPaymentService(get(authQualifier)) }
    single { createLoanService(get(authQualifier)) }
    single { createDonationService(get(authQualifier)) }
}

private fun createUserService(ktorfit: Ktorfit): UserService = ktorfit.create()
private fun createAuthService(ktorfit: Ktorfit): AuthService = ktorfit.create()
private fun createPlanService(ktorfit: Ktorfit): PlanService = ktorfit.create()
private fun createStepService(ktorfit: Ktorfit): StepService = ktorfit.create()
private fun createBudgetService(ktorfit: Ktorfit): BudgetService = ktorfit.create()
private fun createNotificationService(ktorfit: Ktorfit): NotificationService = ktorfit.create()
private fun createFundRequestService(ktorfit: Ktorfit): FundRequestService = ktorfit.create()
private fun createPaymentService(ktorfit: Ktorfit): PaymentService = ktorfit.create()
private fun createLoanService(ktorfit: Ktorfit): LoanService = ktorfit.create()
private fun createDonationService(ktorfit: Ktorfit): DonationService = ktorfit.create()

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