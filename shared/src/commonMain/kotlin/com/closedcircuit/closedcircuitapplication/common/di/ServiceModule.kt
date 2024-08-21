package com.closedcircuit.closedcircuitapplication.common.di

import com.closedcircuit.closedcircuitapplication.common.data.auth.AuthService
import com.closedcircuit.closedcircuitapplication.common.data.notification.NotificationService
import com.closedcircuit.closedcircuitapplication.beneficiary.data.payment.PaymentService
import com.closedcircuit.closedcircuitapplication.common.data.chat.ChatService
import com.closedcircuit.closedcircuitapplication.common.data.user.UserService
import de.jensklingenberg.ktorfit.Ktorfit
import org.koin.dsl.module

val serviceModule = module {
    single { createUserService(get(authQualifier)) }
    single { createAuthService(get(noAuthQualifier)) }
    single { createNotificationService(get(authQualifier)) }
    single { createPaymentService(get(authQualifier)) }
    single { createChatService(get(authQualifier)) }
}

private fun createUserService(ktorfit: Ktorfit): UserService = ktorfit.create()
private fun createAuthService(ktorfit: Ktorfit): AuthService = ktorfit.create()
private fun createNotificationService(ktorfit: Ktorfit): NotificationService = ktorfit.create()
private fun createPaymentService(ktorfit: Ktorfit): PaymentService = ktorfit.create()
private fun createChatService(ktorfit: Ktorfit): ChatService = ktorfit.create()