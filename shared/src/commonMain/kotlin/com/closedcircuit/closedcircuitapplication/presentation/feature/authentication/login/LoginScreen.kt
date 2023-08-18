@file:OptIn(ExperimentalMaterial3Api::class)

package com.closedcircuit.closedcircuitapplication.presentation.feature.authentication.login

//import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.closedcircuit.closedcircuitapplication.presentation.component.BodyText
import com.closedcircuit.closedcircuitapplication.presentation.component.ContentWithMessageBar
import com.closedcircuit.closedcircuitapplication.presentation.component.DefaultAppBar
import com.closedcircuit.closedcircuitapplication.presentation.component.DefaultButton
import com.closedcircuit.closedcircuitapplication.presentation.component.DefaultOutlinedTextField
import com.closedcircuit.closedcircuitapplication.presentation.component.LoadingDialog
import com.closedcircuit.closedcircuitapplication.presentation.component.PasswordOutlinedTextField
import com.closedcircuit.closedcircuitapplication.presentation.component.TitleText
import com.closedcircuit.closedcircuitapplication.presentation.component.rememberMessageBarState
import com.closedcircuit.closedcircuitapplication.presentation.feature.authentication.password_recovery.ResetPasswordEmailScreen
import com.closedcircuit.closedcircuitapplication.presentation.feature.authentication.register.RegisterScreen
import com.closedcircuit.closedcircuitapplication.presentation.feature.home.DashboardScreen
import com.closedcircuit.closedcircuitapplication.presentation.feature.onboarding.WelcomeScreen
import com.closedcircuit.closedcircuitapplication.resources.SharedRes
import dev.icerock.moko.resources.compose.painterResource
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.coroutines.delay
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object LoginScreen : Screen, KoinComponent {
    private val viewModel: LoginViewModel by inject()

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val state = viewModel.state
        val messageBarState = rememberMessageBarState()

        LaunchedEffect(state.loginResult) {
            when (state.loginResult) {
                is LoginResult.Failure -> {
                    messageBarState.addError(Exception(state.loginResult.message))
                    viewModel.onEvent(LoginUIEvent.LoginResultHandled)
                }

                LoginResult.Success -> {
                    delay(500) //wait for loader to hide
                    navigator.replaceAll(DashboardScreen)
                    viewModel.onEvent(LoginUIEvent.LoginResultHandled)
                }

                null -> Unit
            }
        }

        ContentWithMessageBar(messageBarState = messageBarState) {
            ScreenContent(
                state = state,
                onEvent = viewModel::onEvent,
                navigateToWelcomeScreen = { navigator.replaceAll(WelcomeScreen) },
                navigateToCreateAccount = { navigator.push(RegisterScreen) },
                navigateToRecoverPassword = { navigator.push(ResetPasswordEmailScreen) }
            )
        }
    }
}

@Composable
private fun ScreenContent(
    state: LoginUIState,
    onEvent: (LoginUIEvent) -> Unit,
    navigateToWelcomeScreen: () -> Unit,
    navigateToCreateAccount: () -> Unit,
    navigateToRecoverPassword: () -> Unit
) {
    Scaffold(
        topBar = { DefaultAppBar(mainAction = navigateToWelcomeScreen) },
//        contentWindowInsets = WindowInsets.ime
    ) { innerPadding ->

        Box(modifier = Modifier.fillMaxSize()) {
            var showPassword by rememberSaveable { mutableStateOf(false) }
            LazyColumn(
                contentPadding = PaddingValues(bottom = 24.dp),
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(horizontal = 20.dp)
            ) {
                item {
                    TitleText(text = stringResource(SharedRes.strings.welcome_back))

                    Spacer(modifier = Modifier.height(8.dp))
                    BodyText(text = stringResource(SharedRes.strings.login_prompt))

                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(40.dp))
                        Image(
                            painter = painterResource(SharedRes.images.login_illustration),
                            contentDescription = null,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )

                        Spacer(modifier = Modifier.height(40.dp))
                        DefaultOutlinedTextField(
                            value = state.email,
                            onValueChange = { email -> onEvent(LoginUIEvent.EmailChange(email)) },
                            label = stringResource(SharedRes.strings.email),
                            maxLines = 1,
                            keyboardOptions = KeyboardOptions(
                                autoCorrect = false,
                                keyboardType = KeyboardType.Email,
                                imeAction = ImeAction.Next
                            )
                        )

                        Spacer(modifier = Modifier.height(20.dp))
                        PasswordOutlinedTextField(
                            value = state.password,
                            onValueChange = { onEvent(LoginUIEvent.PasswordChange(it)) },
                            label = stringResource(SharedRes.strings.password),
                            onPasswordVisibilityChange = { showPassword = it },
                            showPassword = showPassword,
                            imeAction = ImeAction.Done
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                        BodyText(
                            text = stringResource(SharedRes.strings.forgot_password),
                            color = MaterialTheme.colorScheme.tertiary,
                            modifier = Modifier.align(Alignment.End)
                                .clickable(onClick = navigateToRecoverPassword),
                        )

                        Spacer(modifier = Modifier.height(40.dp))
                        DefaultButton(onClick = { onEvent(LoginUIEvent.Submit) }) {
                            Text(text = stringResource(SharedRes.strings.login))
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        Row {
                            BodyText(text = stringResource(SharedRes.strings.dont_have_account))

                            Spacer(modifier = Modifier.width(4.dp))
                            BodyText(
                                text = stringResource(SharedRes.strings.create_account),
                                color = MaterialTheme.colorScheme.tertiary,
                                modifier = Modifier.clickable(onClick = navigateToCreateAccount)
                            )
                        }
                    }
                }
            }

            if (state.loading) {
                LoadingDialog()
            }
        }
    }
}