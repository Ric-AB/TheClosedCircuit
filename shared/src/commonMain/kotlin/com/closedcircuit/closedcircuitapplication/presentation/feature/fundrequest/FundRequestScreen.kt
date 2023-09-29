package com.closedcircuit.closedcircuitapplication.presentation.feature.fundrequest

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.closedcircuit.closedcircuitapplication.presentation.component.BaseScaffold
import com.closedcircuit.closedcircuitapplication.presentation.component.BodyText
import com.closedcircuit.closedcircuitapplication.presentation.component.DefaultAppBar
import com.closedcircuit.closedcircuitapplication.presentation.component.DefaultButton
import com.closedcircuit.closedcircuitapplication.presentation.component.DefaultOutlinedTextField
import com.closedcircuit.closedcircuitapplication.presentation.component.LargeDropdownMenu
import com.closedcircuit.closedcircuitapplication.presentation.component.TextFieldTrailingText
import com.closedcircuit.closedcircuitapplication.presentation.component.TitleText
import com.closedcircuit.closedcircuitapplication.presentation.component.TopLabeledTextField
import com.closedcircuit.closedcircuitapplication.presentation.theme.defaultHorizontalScreenPadding
import com.closedcircuit.closedcircuitapplication.resources.SharedRes
import com.closedcircuit.closedcircuitapplication.util.InputField
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.collections.immutable.persistentListOf
import org.koin.core.component.KoinComponent

internal object FundRequestScreen : Screen, KoinComponent {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        ScreenContent(goBack = navigator::pop)
    }
}

@Composable
private fun ScreenContent(goBack: () -> Unit) {
    BaseScaffold(topBar = { DefaultAppBar(mainAction = goBack) }) { innerPadding ->
        val commonModifier = Modifier.fillMaxWidth()

        Column(
            modifier = Modifier.fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = defaultHorizontalScreenPadding)
        ) {
            TitleText(text = "How do you want to be supported?")

            Spacer(modifier = Modifier.height(8.dp))
            LargeDropdownMenu(
                modifier = Modifier.fillMaxWidth(),
                label = stringResource(SharedRes.strings.select_category),
                items = persistentListOf(
                    "Loans",
                    "Donations (Cash gifts)",
                    "Both (Loans and Donations)"
                ),
                onItemSelected = { _, item -> },
            )

            Spacer(modifier = Modifier.height(20.dp))
            TitleText(text = "Loan schedule")
            BodyText(text = "Select your preferred loan schedule")

            Text(text = "What's your loan range?")
            DefaultOutlinedTextField(
                inputField = InputField(),
                onValueChange = { },
                placeholder = { Text(text = "Minimum amount") },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Number
                ),
                trailingIcon = { TextFieldTrailingText("NGN") },
                modifier = commonModifier.onFocusChanged {}
            )

            Spacer(modifier = Modifier.height(12.dp))
            DefaultOutlinedTextField(
                inputField = InputField(),
                onValueChange = { },
                placeholder = { Text(text = "Maximum amount") },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Number
                ),
                trailingIcon = { TextFieldTrailingText("NGN") },
                modifier = commonModifier.onFocusChanged {}
            )

            Spacer(modifier = Modifier.height(12.dp))
            LargeDropdownMenu(
                modifier = Modifier.fillMaxWidth(),
                label = "Select maximum number of lenders",
                items = persistentListOf("1", "2", "3"),
                onItemSelected = { _, item -> },
            )

            Spacer(modifier = Modifier.height(12.dp))
            LargeDropdownMenu(
                modifier = Modifier.fillMaxWidth(),
                label = "Select grace duration",
                items = persistentListOf("3", "6", "9", "12", "15", "18"),
                onItemSelected = { _, item -> },
            )

            Spacer(modifier = Modifier.height(12.dp))
            LargeDropdownMenu(
                modifier = Modifier.fillMaxWidth(),
                label = "Select repayment duration",
                items = persistentListOf("3", "6", "9", "12", "15", "18"),
                onItemSelected = { _, item -> },
            )

            Spacer(modifier = Modifier.height(12.dp))
            TopLabeledTextField(
                inputField = InputField(),
                onValueChange = { },
                label = "Enter loan interest rate (%)",
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Number
                ),
                trailingIcon = { TextFieldTrailingText("%") },
                modifier = commonModifier.onFocusChanged {}
            )

            Spacer(modifier = Modifier.height(40.dp))
            DefaultButton(onClick = {}) {
                Text(text = "Proceed")
            }
        }
    }
}