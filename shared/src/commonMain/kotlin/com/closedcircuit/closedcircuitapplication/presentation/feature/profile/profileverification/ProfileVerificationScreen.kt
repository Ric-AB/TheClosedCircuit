package com.closedcircuit.closedcircuitapplication.presentation.feature.profile.profileverification

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.closedcircuit.closedcircuitapplication.domain.model.Email
import com.closedcircuit.closedcircuitapplication.presentation.component.BaseScaffold
import com.closedcircuit.closedcircuitapplication.presentation.component.BodyText
import com.closedcircuit.closedcircuitapplication.presentation.component.DefaultAppBar
import com.closedcircuit.closedcircuitapplication.presentation.component.MessageBarState
import com.closedcircuit.closedcircuitapplication.presentation.component.OtpView
import com.closedcircuit.closedcircuitapplication.presentation.component.SuccessScreen
import com.closedcircuit.closedcircuitapplication.presentation.component.TitleText
import com.closedcircuit.closedcircuitapplication.presentation.component.rememberMessageBarState
import com.closedcircuit.closedcircuitapplication.presentation.feature.profile.home.ProfileScreen
import com.closedcircuit.closedcircuitapplication.presentation.navigation.transition.CustomScreenTransition
import com.closedcircuit.closedcircuitapplication.presentation.navigation.transition.ModalTransition
import com.closedcircuit.closedcircuitapplication.presentation.theme.defaultHorizontalScreenPadding
import com.closedcircuit.closedcircuitapplication.resources.SharedRes
import com.closedcircuit.closedcircuitapplication.util.observerWithScreen
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf

internal class ProfileVerificationScreen(val email: Email) : Screen, KoinComponent,
    CustomScreenTransition by ModalTransition {
    private val viewModel: ProfileVerificationViewModel by inject { parametersOf(email) }

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val uiState = viewModel.state
        val messageBarState = rememberMessageBarState()
        val onEvent = viewModel::onEvent
        var otpError by remember { mutableStateOf(false) }
        val onOtpChange: (String, Boolean) -> Unit = { text, codeComplete ->
            otpError = false
            onEvent(ProfileVerificationUIEvent.OtpChange(text))
            if (codeComplete) {
                onEvent(ProfileVerificationUIEvent.Submit)
            }
        }

        viewModel.resultChannel.receiveAsFlow().observerWithScreen {
            when (it) {
                is ProfileVerificationResult.RequestOtpFailure -> messageBarState.addError(it.message)
                ProfileVerificationResult.RequestOtpSuccess -> messageBarState.addSuccess("Otp code has been sent to ${email.value}")
                is ProfileVerificationResult.VerifyOtpFailure -> otpError = true
                ProfileVerificationResult.VerifyOtpSuccess -> {
                    delay(300)
                    navigator.push(
                        SuccessScreen(
                            title = "Email verification successful",
                            message = "",
                            primaryAction = { navigator.popUntil { screen -> screen == ProfileScreen } }
                        )
                    )
                }
            }
        }

        ScreenContent(
            messageBarState = messageBarState,
            uiState = uiState,
            otpError = otpError,
            otpChange = onOtpChange,
            resendOtp = { onEvent(ProfileVerificationUIEvent.RequestOtp(isResend = true)) },
            goBack = navigator::pop
        )
    }
}

@Composable
private fun ScreenContent(
    messageBarState: MessageBarState,
    uiState: ProfileVerificationUIState,
    otpError: Boolean,
    otpChange: (String, Boolean) -> Unit,
    resendOtp: () -> Unit,
    goBack: () -> Unit
) {
    BaseScaffold(
        messageBarState = messageBarState,
        isLoading = uiState.isLoading,
        topBar = {
            DefaultAppBar(
                mainIcon = Icons.Rounded.Close,
                mainAction = goBack
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = defaultHorizontalScreenPadding)
                .windowInsetsPadding(WindowInsets.ime)
        ) {
            TitleText(text = stringResource(SharedRes.strings.verify_email_address))

            Spacer(modifier = Modifier.height(8.dp))
            BodyText(text = stringResource(SharedRes.strings.verification_code_prompt))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                OtpView(
                    otpCode = uiState.otpCodeField.value,
                    itemCount = 6,
                    isError = otpError,
                ) { text, codeComplete ->
                    otpChange(text, codeComplete)
                }

                Spacer(modifier = Modifier.height(12.dp))
                Row {
                    BodyText(text = stringResource(SharedRes.strings.didnt_receive_code))

                    Spacer(modifier = Modifier.width(4.dp))
                    BodyText(
                        text = stringResource(SharedRes.strings.resend),
                        color = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier.clickable { resendOtp() }
                    )
                }

            }
        }
    }
}