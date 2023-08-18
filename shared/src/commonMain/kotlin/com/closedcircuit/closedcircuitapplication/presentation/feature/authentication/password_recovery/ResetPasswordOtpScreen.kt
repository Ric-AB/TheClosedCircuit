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
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.closedcircuit.closedcircuitapplication.presentation.component.BodyText
import com.closedcircuit.closedcircuitapplication.presentation.component.DefaultAppBar
import com.closedcircuit.closedcircuitapplication.presentation.component.LoadingDialog
import com.closedcircuit.closedcircuitapplication.presentation.component.OtpView
import com.closedcircuit.closedcircuitapplication.presentation.component.TitleText
import com.closedcircuit.closedcircuitapplication.presentation.component.rememberMessageBarState
import com.closedcircuit.closedcircuitapplication.resources.SharedRes
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.coroutines.delay
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

        LaunchedEffect(state.verifyOtpResult) {
            when (state.verifyOtpResult) {
                VerifyOtpResult.Success -> {
                    delay(500)
                    navigator.push(NewPasswordScreen)
                    onEvent(ResetPasswordUIEvent.ResultHandled)
                }

                else -> Unit
            }
        }
        ScreenContent(
            state = state,
            onEvent = onEvent,
            goBack = navigator::pop
        )
    }
}

@Composable
private fun ScreenContent(
    state: ResetPasswordUIState,
    onEvent: (ResetPasswordUIEvent) -> Unit,
    goBack: () -> Unit
) {
    Scaffold(topBar = { DefaultAppBar(mainAction = goBack) }) { innerPadding ->
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
                    otpCode = state.otpCode,
                    itemCount = 6,
                    isError = state.verifyOtpResult is VerifyOtpResult.Failure,
                    itemHeight = 45.dp,
                    itemWidth = 45.dp
                ) { text, codeComplete ->
                    onEvent(ResetPasswordUIEvent.OtpCodeChange(text))
                    if (codeComplete) onEvent(ResetPasswordUIEvent.SubmitOtp)
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