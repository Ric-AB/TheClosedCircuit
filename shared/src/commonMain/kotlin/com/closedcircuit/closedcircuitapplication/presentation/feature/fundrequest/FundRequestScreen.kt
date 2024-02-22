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
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import org.koin.core.component.KoinComponent

internal class FundRequestScreen : Screen, KoinComponent {
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
            TitleText(text = stringResource(SharedRes.strings.how_do_you_want_to_be_supported_label))

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
            TitleText(stringResource(SharedRes.strings.loan_schedule_label))
            BodyText(stringResource(SharedRes.strings.select_your_preferred_loan_schedule_label))

            Text(text = stringResource(SharedRes.strings.what_your_loan_range_label))
            DefaultOutlinedTextField(
                inputField = InputField(),
                onValueChange = { },
                placeholder = { Text(stringResource(SharedRes.strings.minimum_amount_label)) },
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
                placeholder = { Text(stringResource(SharedRes.strings.maximum_amount_label)) },
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
                label = stringResource(SharedRes.strings.select_maximum_number_of_lenders_label),
                items = getNumberOfLenders(),
                onItemSelected = { _, item -> },
            )

            Spacer(modifier = Modifier.height(12.dp))
            LargeDropdownMenu(
                modifier = Modifier.fillMaxWidth(),
                label = stringResource(SharedRes.strings.select_grace_duration_label),
                items = getDurations(),
                onItemSelected = { _, item -> },
            )

            Spacer(modifier = Modifier.height(12.dp))
            LargeDropdownMenu(
                modifier = Modifier.fillMaxWidth(),
                label = stringResource(SharedRes.strings.select_repayment_duration_label),
                items = getDurations(),
                onItemSelected = { _, item -> },
            )

            Spacer(modifier = Modifier.height(12.dp))
            TopLabeledTextField(
                inputField = InputField(),
                onValueChange = { },
                label = stringResource(SharedRes.strings.enter_loan_interest_rate_label),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Next,
                    keyboardType = KeyboardType.Number
                ),
                trailingIcon = { TextFieldTrailingText("%") },
                modifier = commonModifier.onFocusChanged {}
            )

            Spacer(modifier = Modifier.height(40.dp))
            DefaultButton(onClick = {}) {
                Text(stringResource(SharedRes.strings.proceed))
            }
        }
    }
}

private fun getDurations(): ImmutableList<String> {
    return persistentListOf("3", "6", "9", "12", "15", "18")
}

private fun getNumberOfLenders(): ImmutableList<String> {
    return persistentListOf("1", "2", "3")
}