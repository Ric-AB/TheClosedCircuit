@file:OptIn(ExperimentalMaterial3Api::class)

package com.closedcircuit.closedcircuitapplication.presentation.feature.authentication.password_recovery

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.closedcircuit.closedcircuitapplication.presentation.component.BodyText
import com.closedcircuit.closedcircuitapplication.presentation.component.DefaultAppBar
import com.closedcircuit.closedcircuitapplication.presentation.component.DefaultButton
import com.closedcircuit.closedcircuitapplication.presentation.component.DefaultOutlinedTextField
import com.closedcircuit.closedcircuitapplication.presentation.component.TitleText
import com.closedcircuit.closedcircuitapplication.resources.SharedRes
import dev.icerock.moko.resources.compose.painterResource
import dev.icerock.moko.resources.compose.stringResource
import org.koin.core.component.KoinComponent


internal object RecoverPasswordEmailScreen : Screen, KoinComponent {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        ScreenContent(goBack = navigator::pop)
    }
}

@Composable
private fun ScreenContent(goBack: () -> Unit) {
    Scaffold(topBar = { DefaultAppBar(mainAction = goBack) }) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
        ) {
            TitleText(text = stringResource(SharedRes.strings.password_recovery))

            Spacer(modifier = Modifier.height(8.dp))
            BodyText(text = stringResource(SharedRes.strings.we_are_here_to_help_you_recover_password))

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(modifier = Modifier.height(8.dp))
                Image(
                    painter = painterResource(SharedRes.images.recover_password_illurstration),
                    contentDescription = null
                )

                Spacer(modifier = Modifier.height(40.dp))
                DefaultOutlinedTextField(
                    value = "",
                    onValueChange = {},
                    label = stringResource(SharedRes.strings.email),
                    keyboardOptions = KeyboardOptions(
                        autoCorrect = false,
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Done
                    )
                )

                Spacer(modifier = Modifier.height(20.dp))
                DefaultButton(onClick = {}) {
                    Text(stringResource(SharedRes.strings.recover_password))
                }

                Spacer(modifier = Modifier.height(12.dp))
                Row {
                    BodyText(text = stringResource(SharedRes.strings.remember_password))

                    Spacer(modifier = Modifier.width(4.dp))
                    BodyText(
                        text = stringResource(SharedRes.strings.login),
                        color = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier.clickable(onClick = goBack)
                    )
                }
            }
        }
    }
}