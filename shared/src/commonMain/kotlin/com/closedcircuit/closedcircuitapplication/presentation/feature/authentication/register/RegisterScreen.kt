@file:OptIn(ExperimentalMaterial3Api::class)

package com.closedcircuit.closedcircuitapplication.presentation.feature.authentication.register

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.closedcircuit.closedcircuitapplication.presentation.component.BodyText
import com.closedcircuit.closedcircuitapplication.presentation.component.DefaultAppBar
import com.closedcircuit.closedcircuitapplication.presentation.component.DefaultButton
import com.closedcircuit.closedcircuitapplication.presentation.component.DefaultOutlinedTextField
import com.closedcircuit.closedcircuitapplication.presentation.component.TitleText
import com.closedcircuit.closedcircuitapplication.presentation.component.icon.rememberVisibility
import com.closedcircuit.closedcircuitapplication.presentation.component.icon.rememberVisibilityOff
import com.closedcircuit.closedcircuitapplication.presentation.feature.authentication.login.LoginScreen
import com.closedcircuit.closedcircuitapplication.resources.SharedRes
import com.moriatsushi.insetsx.imePadding
import dev.icerock.moko.resources.compose.stringResource
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

object RegisterScreen : Screen, KoinComponent {
    private val viewModel: RegisterViewModel by inject()

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val state = viewModel.state
        ScreenContent(
            state = state,
            onEvent = viewModel::onEvent,
            navigateToLogin = { navigator.replaceAll(LoginScreen) }
        )
    }
}

@Composable
private fun ScreenContent(
    state: RegisterUIState,
    onEvent: (RegisterUIEvent) -> Unit,
    navigateToLogin: () -> Unit
) {
    Scaffold(topBar = { DefaultAppBar(mainAction = navigateToLogin) }) { innerPadding ->

        var showPassword by rememberSaveable { mutableStateOf(false) }
        var showConfirmPassword by rememberSaveable { mutableStateOf(false) }
        LazyColumn(
            contentPadding = PaddingValues(bottom = 24.dp),
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .imePadding()
        ) {
            item {
                BodyText(
                    text = stringResource(SharedRes.strings.shared_build_shared_prosperity),
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(8.dp))
                TitleText(text = stringResource(SharedRes.strings.create_a_new_account))

                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(20.dp))
                    DefaultOutlinedTextField(
                        value = state.firstName,
                        onValueChange = { onEvent(RegisterUIEvent.FirstNameChange(it)) },
                        label = stringResource(SharedRes.strings.first_name),
                        keyboardOptions = KeyboardOptions(
                            autoCorrect = false,
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next,
                            capitalization = KeyboardCapitalization.Words
                        ),
                    )

                    DefaultOutlinedTextField(
                        value = state.nickName,
                        onValueChange = { onEvent(RegisterUIEvent.NickNameChange(it)) },
                        label = stringResource(SharedRes.strings.preferred) + "/" +
                                stringResource(SharedRes.strings.nick_name),
                        keyboardOptions = KeyboardOptions(
                            autoCorrect = false,
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next,
                            capitalization = KeyboardCapitalization.Words
                        ),
                    )

                    DefaultOutlinedTextField(
                        value = state.lastName,
                        onValueChange = { onEvent(RegisterUIEvent.LastNameChange(it)) },
                        label = stringResource(SharedRes.strings.last_name),
                        keyboardOptions = KeyboardOptions(
                            autoCorrect = false,
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next,
                            capitalization = KeyboardCapitalization.Words
                        ),
                    )

                    DefaultOutlinedTextField(
                        value = state.email,
                        onValueChange = { onEvent(RegisterUIEvent.EmailChange(it)) },
                        label = stringResource(SharedRes.strings.email),
                        keyboardOptions = KeyboardOptions(
                            autoCorrect = false,
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        ),
                    )

                    DefaultOutlinedTextField(
                        value = state.phoneNumber,
                        onValueChange = { onEvent(RegisterUIEvent.PhoneNumberChange(it)) },
                        label = stringResource(SharedRes.strings.phone_number),
                        keyboardOptions = KeyboardOptions(
                            autoCorrect = false,
                            keyboardType = KeyboardType.Phone,
                            imeAction = ImeAction.Next
                        ),
                    )

                    Spacer(modifier = Modifier.height(40.dp))
                    DefaultOutlinedTextField(
                        value = state.password,
                        onValueChange = { onEvent(RegisterUIEvent.PasswordChange(it)) },
                        label = stringResource(SharedRes.strings.password),
                        trailingIcon = {
                            IconButton(onClick = { showPassword = !showPassword }) {
                                Icon(
                                    imageVector = if (showPassword) rememberVisibilityOff() else rememberVisibility(),
                                    contentDescription = null
                                )
                            }
                        },
                        visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(
                            autoCorrect = false,
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Next
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    DefaultOutlinedTextField(
                        value = state.confirmPassword,
                        onValueChange = { onEvent(RegisterUIEvent.ConfirmPasswordChange(it)) },
                        label = stringResource(SharedRes.strings.confirm_password),
                        trailingIcon = {
                            IconButton(onClick = { showConfirmPassword = !showConfirmPassword }) {
                                Icon(
                                    imageVector = if (showConfirmPassword) rememberVisibilityOff() else rememberVisibility(),
                                    contentDescription = null
                                )
                            }
                        },
                        visualTransformation = if (showConfirmPassword) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(
                            autoCorrect = false,
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(40.dp))
                    DefaultButton(onClick = {}) {
                        Text(text = stringResource(SharedRes.strings.create_account))
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Row {
                        BodyText(text = stringResource(SharedRes.strings.already_have_an_account))

                        Spacer(modifier = Modifier.width(4.dp))
                        BodyText(
                            text = stringResource(SharedRes.strings.login),
                            color = MaterialTheme.colorScheme.tertiary,
                            modifier = Modifier.clickable(onClick = navigateToLogin)
                        )
                    }
                }
            }
        }
    }
}