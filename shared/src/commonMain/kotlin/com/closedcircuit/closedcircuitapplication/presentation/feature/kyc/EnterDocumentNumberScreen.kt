package com.closedcircuit.closedcircuitapplication.presentation.feature.kyc

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.closedcircuit.closedcircuitapplication.domain.model.KycDocumentType
import com.closedcircuit.closedcircuitapplication.presentation.component.BaseScaffold
import com.closedcircuit.closedcircuitapplication.presentation.component.DefaultAppBar
import com.closedcircuit.closedcircuitapplication.presentation.component.DefaultButton
import com.closedcircuit.closedcircuitapplication.presentation.component.MessageBarState
import com.closedcircuit.closedcircuitapplication.presentation.component.PlaceHolderText
import com.closedcircuit.closedcircuitapplication.presentation.component.TopLabeledTextField
import com.closedcircuit.closedcircuitapplication.presentation.component.rememberMessageBarState
import com.closedcircuit.closedcircuitapplication.presentation.theme.horizontalScreenPadding
import com.closedcircuit.closedcircuitapplication.presentation.theme.verticalScreenPadding
import com.closedcircuit.closedcircuitapplication.resources.SharedRes
import com.closedcircuit.closedcircuitapplication.util.observeWithScreen
import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.coroutines.flow.receiveAsFlow
import org.koin.core.component.KoinComponent


internal class EnterDocumentNumberScreen : Screen, KoinComponent {
    @Composable
    override fun Content() {
        val messageBarState = rememberMessageBarState()
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = navigator.getNavigatorScreenModel<KycViewModel>()

        viewModel.resultChannel.receiveAsFlow().observeWithScreen {
            when (it) {
                is KycResult.Failure -> messageBarState.addError(it.message)
                KycResult.Success -> navigator.replaceAll(KycStatusScreen())
            }
        }

        viewModel.state?.let {
            ScreenContent(
                state = it,
                messageBarState = messageBarState,
                goBack = navigator::pop,
                onEvent = viewModel::onEvent
            )
        }

    }

    @Composable
    private fun ScreenContent(
        state: KycUiState,
        messageBarState: MessageBarState,
        goBack: () -> Unit,
        onEvent: (KycUiEvent) -> Unit
    ) {
        BaseScaffold(
            topBar = {
                DefaultAppBar(
                    title = stringResource(SharedRes.strings.input_verification_details),
                    mainAction = goBack
                )
            },
            showLoadingDialog = state.isLoading,
            messageBarState = messageBarState
        ) { innerPadding ->
            val placeHolderAndHintResource =
                remember { getTextResources(state.selectedDocumentType) }

            Column(
                modifier = Modifier.fillMaxSize()
                    .padding(innerPadding)
                    .padding(
                        horizontal = horizontalScreenPadding,
                        vertical = verticalScreenPadding
                    ).imePadding()
            ) {
                TopLabeledTextField(
                    inputField = state.documentNumber,
                    onValueChange = { onEvent(KycUiEvent.DocumentNumberChange(it)) },
                    label = "",
                    singleLine = true,
                    placeholder = {
                        placeHolderAndHintResource?.first?.let {
                            PlaceHolderText(text = stringResource(it))
                        }
                    },
                    supportingText = {
                        placeHolderAndHintResource?.second?.let {
                            Hint(stringResource(it))
                        }
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                )

                Spacer(modifier = Modifier.weight(1f))
                DefaultButton(onClick = { onEvent(KycUiEvent.Submit) }, enabled = state.canSubmit) {
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

    private fun getTextResources(type: KycDocumentType?): Pair<StringResource, StringResource?>? {
        return when (type) {
            KycDocumentType.BVN -> Pair(
                SharedRes.strings.enter_bvn,
                SharedRes.strings.check_bvn_hint
            )

            KycDocumentType.NIN_SLIP -> Pair(
                SharedRes.strings.enter_nin,
                SharedRes.strings.check_nin_hint
            )

            KycDocumentType.PHONE_NUMBER -> Pair(SharedRes.strings.enter_phone_number, null)
            KycDocumentType.NATIONAL_ID_NO_PHOTO -> Pair(SharedRes.strings.enter_nin, null)
            else -> null
        }
    }
}