package com.closedcircuit.closedcircuitapplication.common.di

import com.closedcircuit.closedcircuitapplication.common.presentation.feature.authentication.login.LoginViewModel
import com.closedcircuit.closedcircuitapplication.common.presentation.feature.authentication.passwordrecovery.ResetPasswordKoinContainer
import com.closedcircuit.closedcircuitapplication.common.presentation.feature.authentication.passwordrecovery.ResetPasswordViewModel
import com.closedcircuit.closedcircuitapplication.common.presentation.feature.authentication.register.RegisterViewModel
import com.closedcircuit.closedcircuitapplication.common.presentation.feature.chat.conversation.ConversationViewModel
import com.closedcircuit.closedcircuitapplication.common.presentation.feature.chat.conversationlist.ConversationListViewModel
import com.closedcircuit.closedcircuitapplication.common.presentation.feature.chat.conversationpartners.ConversationPartnersViewModel
import com.closedcircuit.closedcircuitapplication.common.presentation.feature.notification.NotificationViewModel
import com.closedcircuit.closedcircuitapplication.common.presentation.feature.onboarding.OnboardingViewModel
import com.closedcircuit.closedcircuitapplication.common.presentation.feature.payment.PaymentViewModel
import com.closedcircuit.closedcircuitapplication.common.presentation.feature.profile.changepassword.ChangePasswordViewModel
import com.closedcircuit.closedcircuitapplication.common.presentation.feature.profile.edit.EditProfileViewModel
import com.closedcircuit.closedcircuitapplication.common.presentation.feature.profile.home.ProfileTabViewModel
import com.closedcircuit.closedcircuitapplication.common.presentation.feature.profile.profileverification.EmailVerificationViewModel
import com.closedcircuit.closedcircuitapplication.common.presentation.feature.settings.SettingsViewModel
import com.closedcircuit.closedcircuitapplication.common.presentation.navigation.RootViewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val viewModelModule = module {
    factory { RootViewModel(get(), get(), get(), get()) }

    // onboarding
    factory { OnboardingViewModel(get()) }
    factory { LoginViewModel(get(), get()) }
    factory { RegisterViewModel(get(), get(), get()) }
    single { ResetPasswordKoinContainer() } // todo replace with scoped navigator
    scope<ResetPasswordKoinContainer> {
        scoped { ResetPasswordViewModel(get()) }
    }

    // profile
    factory {
        ProfileTabViewModel(
            planRepository = get(),
            userRepository = get(),
            imageStorageReference = get(named(namedImageStorageReference)),
        )
    }
    factory { EditProfileViewModel(get()) }
    factory { parameters -> EmailVerificationViewModel(parameters.get(), get(), get()) }
    factory { ChangePasswordViewModel(get()) }

    // notification
    factory { NotificationViewModel(get()) }

    // settings
    factory { SettingsViewModel(get(), get(), get()) }

    // payment
    factory { PaymentViewModel() }

    // chat
    factory { parameters -> ConversationPartnersViewModel(parameters.get(), get(), get()) }
    factory { parameters -> ConversationViewModel(parameters.get(), get(), get()) }
    factory { ConversationListViewModel(get(), get()) }
}