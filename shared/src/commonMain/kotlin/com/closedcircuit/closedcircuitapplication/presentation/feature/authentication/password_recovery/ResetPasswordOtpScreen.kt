@file:OptIn(ExperimentalMaterial3Api::class)

package com.closedcircuit.closedcircuitapplication.presentation.feature.authentication.password_recovery

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.closedcircuit.closedcircuitapplication.presentation.component.BaseScaffold
import com.closedcircuit.closedcircuitapplication.presentation.component.BodyText
import com.closedcircuit.closedcircuitapplication.presentation.component.DefaultAppBar
import com.closedcircuit.closedcircuitapplication.presentation.component.LoadingDialog
import com.closedcircuit.closedcircuitapplication.presentation.component.MessageBarState
import com.closedcircuit.closedcircuitapplication.presentation.component.OtpView
import com.closedcircuit.closedcircuitapplication.presentation.component.TitleText
import com.closedcircuit.closedcircuitapplication.presentation.component.rememberMessageBarState
import com.closedcircuit.closedcircuitapplication.presentation.util.observerWithScreen
import com.closedcircuit.closedcircuitapplication.resources.SharedRes
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.coroutines.flow.receiveAsFlow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

internal object ResetPasswordOtpScreen : Screen, KoinComponent {
    private val resetPasswordKoinContainer: ResetPasswordKoinContainer by inject()
    private val viewModel: ResetPasswordViewModel = resetPasswordKoinContainer.scope.get()

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val state = viewModel.state
        val onEvent = viewModel::onEvent
        val messageBarState = rememberMessageBarState()
        var otpError by remember { mutableStateOf(false) }
        val onOtpChange: (String, Boolean) -> Unit = { text, codeComplete ->
            otpError = false
            onEvent(ResetPasswordUIEvent.OtpCodeChange(text))
            if (codeComplete) {
                onEvent(ResetPasswordUIEvent.SubmitOtp)
            }
        }

        viewModel.requestOtpResult.receiveAsFlow().observerWithScreen {
            when (it) {
                is RequestOtpResult.Failure -> {
                    messageBarState.addError(it.message)
                }

                is RequestOtpResult.Success -> {
                    if (it.isResend)
                        messageBarState.addSuccess("Otp code has been sent to ${state.emailField.value}")
                }
            }
        }

        viewModel.verifyOtpResult.receiveAsFlow().observerWithScreen {
            when (it) {
                is VerifyOtpResult.Failure -> {
                    otpError = true
                }

                VerifyOtpResult.Success -> {
                    navigator.push(NewPasswordScreen)
                }
            }
        }

        ScreenContent(
            messageBarState = messageBarState,
            state = state,
            otpChange = onOtpChange,
            resendOtp = { onEvent(ResetPasswordUIEvent.RequestOtp(true)) },
            otpError = otpError,
            goBack = navigator::pop
        )
    }
}

@Composable
private fun ScreenContent(
    messageBarState: MessageBarState,
    state: ResetPasswordUIState,
    otpError: Boolean,
    otpChange: (String, Boolean) -> Unit,
    resendOtp: () -> Unit,
    goBack: () -> Unit
) {
    BaseScaffold(
        topBar = { DefaultAppBar(mainAction = goBack) },
        messageBarState = messageBarState,
        isLoading = state.loading
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
        ) {
            TitleText(text = stringResource(SharedRes.strings.password_recovery))

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(modifier = Modifier.height(8.dp))
                BodyText(text = stringResource(SharedRes.strings.youre_almost_close_to_recovering_your_password))

                Spacer(modifier = Modifier.height(40.dp))
                OtpView(
                    otpCode = state.otpCodeField.value,
                    itemCount = 6,
                    isError = otpError,
                    itemHeight = 45.dp,
                    itemWidth = 45.dp
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
                        modifier = Modifier.clickable { }
                    )
                }

            }
        }

        if (state.loading) {
            LoadingDialog()
        }
    }
}