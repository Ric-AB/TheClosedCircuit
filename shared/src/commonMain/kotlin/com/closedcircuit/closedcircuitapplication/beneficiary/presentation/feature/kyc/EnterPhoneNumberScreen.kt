package com.closedcircuit.closedcircuitapplication.beneficiary.presentation.feature.kyc

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.closedcircuit.closedcircuitapplication.common.presentation.components.BaseScaffold
import com.closedcircuit.closedcircuitapplication.common.presentation.components.DefaultAppBar
import com.closedcircuit.closedcircuitapplication.common.presentation.components.DefaultButton
import com.closedcircuit.closedcircuitapplication.common.presentation.components.PlaceHolderText
import com.closedcircuit.closedcircuitapplication.common.presentation.components.TopLabeledTextField
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.horizontalScreenPadding
import com.closedcircuit.closedcircuitapplication.resources.SharedRes
import com.closedcircuit.closedcircuitapplication.common.presentation.util.InputField
import dev.icerock.moko.resources.compose.stringResource
import org.koin.core.component.KoinComponent


internal class EnterPhoneNumberScreen : Screen, KoinComponent {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        ScreenContent(goBack = navigator::pop)
    }
}

@Composable
private fun ScreenContent(goBack: () -> Unit) {
    BaseScaffold(
        topBar = {
            DefaultAppBar(
                title = stringResource(SharedRes.strings.verify_phone_number),
                mainAction = goBack
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = horizontalScreenPadding)
        ) {
            TopLabeledTextField(
                inputField = InputField(),
                onValueChange = {},
                label = stringResource(SharedRes.strings.enter_phone_number),
                singleLine = true,
                placeholder = { PlaceHolderText(text = "080XXXXXXXX") },
                supportingText = { Text(text = stringResource(SharedRes.strings.enter_registered_phone_number)) },
                leadingIcon = { Text(text = "+234") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
            )

            Spacer(modifier = Modifier.weight(1f))
            DefaultButton(onClick = {}) {
                Text(text = stringResource(SharedRes.strings.verify))
            }
        }
    }
}