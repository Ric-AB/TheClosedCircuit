package com.closedcircuit.closedcircuitapplication.sponsor.presentation.feature.makeoffer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.closedcircuit.closedcircuitapplication.common.presentation.components.BaseScaffold
import com.closedcircuit.closedcircuitapplication.common.presentation.components.BodyText
import com.closedcircuit.closedcircuitapplication.common.presentation.components.DefaultAppBar
import com.closedcircuit.closedcircuitapplication.common.presentation.components.DefaultButton
import com.closedcircuit.closedcircuitapplication.common.presentation.components.DefaultOutlinedTextField
import com.closedcircuit.closedcircuitapplication.common.presentation.components.TopAppBarTitle
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.horizontalScreenPadding
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.verticalScreenPadding
import com.closedcircuit.closedcircuitapplication.common.util.InputField
import com.closedcircuit.closedcircuitapplication.resources.SharedRes
import dev.icerock.moko.resources.compose.stringResource
import org.koin.core.component.KoinComponent


internal class LoanTermsScreen : Screen, KoinComponent {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        ScreenContent(goBack = navigator::pop)
    }

    @Composable
    private fun ScreenContent(goBack: () -> Unit) {
        BaseScaffold(topBar = { DefaultAppBar(mainAction = goBack) }) { innerPadding ->
            Column(
                modifier = Modifier.fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = horizontalScreenPadding, vertical = verticalScreenPadding)
            ) {
                TopAppBarTitle(stringResource(SharedRes.strings.loan_terms_label))
                BodyText(stringResource(SharedRes.strings.review_loan_terms_message_label))

                Spacer(Modifier.height(24.dp))
                ScreenTextField("6 Months", "Grace duration")

                Spacer(Modifier.height(8.dp))
                ScreenTextField("6 Months", "Grace duration")

                Spacer(Modifier.height(8.dp))
                ScreenTextField("6 Months", "Grace duration")

                Spacer(Modifier.height(40.dp))
                DefaultButton(onClick = {}) {
                    Text(stringResource(SharedRes.strings.proceed))
                }
            }
        }
    }

    @Composable
    private fun ScreenTextField(text: String, label: String) {
        DefaultOutlinedTextField(
            inputField = InputField(inputValue = mutableStateOf(text)),
            onValueChange = {},
            label = label,
            enabled = false
        )
    }
}