package com.closedcircuit.closedcircuitapplication.presentation.feature.kyc

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.closedcircuit.closedcircuitapplication.domain.user.KycVerificationType
import com.closedcircuit.closedcircuitapplication.presentation.component.BaseScaffold
import com.closedcircuit.closedcircuitapplication.presentation.component.DefaultAppBar
import com.closedcircuit.closedcircuitapplication.presentation.component.DefaultButton
import com.closedcircuit.closedcircuitapplication.presentation.component.PlaceHolderText
import com.closedcircuit.closedcircuitapplication.presentation.component.TopLabeledTextField
import com.closedcircuit.closedcircuitapplication.presentation.theme.defaultHorizontalScreenPadding
import com.closedcircuit.closedcircuitapplication.presentation.theme.defaultVerticalScreenPadding
import com.closedcircuit.closedcircuitapplication.resources.SharedRes
import com.closedcircuit.closedcircuitapplication.util.InputField
import dev.icerock.moko.resources.compose.stringResource
import org.koin.core.component.KoinComponent


internal class EnterVerificationNumberScreen : Screen, KoinComponent {
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
                title = stringResource(SharedRes.strings.input_verification_details),
                mainAction = goBack
            )
        }
    ) { innerPadding ->
        val type = KycVerificationType.BVN
        val placeHolderAndHintResource = remember {
            when (type) {
                KycVerificationType.BVN -> Pair(
                    SharedRes.strings.enter_bvn,
                    SharedRes.strings.check_bvn_hint
                )

                KycVerificationType.NIN -> Pair(
                    SharedRes.strings.enter_nin,
                    SharedRes.strings.check_nin_hint
                )

                KycVerificationType.PHONE_NUMBER -> Pair(SharedRes.strings.enter_phone_number, null)
                KycVerificationType.NATIONAL_ID_NO_PHOTO -> Pair(SharedRes.strings.enter_nin, null)
            }
        }

        Column(
            modifier = Modifier.fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = defaultHorizontalScreenPadding)
                .padding(vertical = defaultVerticalScreenPadding)
        ) {
            TopLabeledTextField(
                inputField = InputField(),
                onValueChange = {},
                label = "",
                singleLine = true,
                placeholder = { PlaceHolderText(text = stringResource(placeHolderAndHintResource.first)) },
                supportingText = {
                    placeHolderAndHintResource.second?.let {
                        Hint(stringResource(it))
                    }
                },
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

@Composable
private fun Hint(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelMedium
    )
}