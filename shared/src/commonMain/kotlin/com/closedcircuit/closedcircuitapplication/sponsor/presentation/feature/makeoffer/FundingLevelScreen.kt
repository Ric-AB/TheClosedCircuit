package com.closedcircuit.closedcircuitapplication.sponsor.presentation.feature.makeoffer

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.closedcircuit.closedcircuitapplication.common.presentation.component.DefaultButton
import com.closedcircuit.closedcircuitapplication.common.presentation.component.TextFieldDialogMenu
import com.closedcircuit.closedcircuitapplication.common.presentation.component.TitleText
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.horizontalScreenPadding
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.verticalScreenPadding
import com.closedcircuit.closedcircuitapplication.common.util.capitalizeFirstChar
import com.closedcircuit.closedcircuitapplication.resources.SharedRes
import com.closedcircuit.closedcircuitapplication.sponsor.domain.model.FundingLevel
import dev.icerock.moko.resources.compose.stringResource
import org.koin.core.component.KoinComponent


internal class FundingLevelScreen : Screen, KoinComponent {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = navigator.getNavigatorScreenModel<MakeOfferViewModel>()
        val state = viewModel.fundingLevelState

        ScreenContent(
            state = state,
            onEvent = viewModel::onEvent,
            navigate = {
                if (state.fundingLevel == FundingLevel.OTHER) navigator.push(EnterOfferAmountScreen())
                else navigator.push(FundingItemsScreen())
            }
        )
    }

    @Composable
    private fun ScreenContent(
        state: FundingLevelUiState,
        onEvent: (MakeOfferEvent) -> Unit,
        navigate: () -> Unit,
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
                .padding(horizontal = horizontalScreenPadding, vertical = verticalScreenPadding)
        ) {
            TitleText(stringResource(SharedRes.strings.how_would_you_like_to_sponsor_label))

            Spacer(Modifier.height(24.dp))
            TextFieldDialogMenu(
                modifier = Modifier.fillMaxWidth(),
                label = stringResource(SharedRes.strings.select_funding_level_label),
                items = state.fundingLevels,
                itemToString = { it.requestValue.capitalizeFirstChar() },
                selectedItem = state.fundingLevel,
                onItemSelected = { _, item -> onEvent(MakeOfferEvent.FundingLevelChange(item)) },
            )

            Spacer(Modifier.height(40.dp))
            DefaultButton(onClick = navigate, enabled = state.canProceed) {
                Text(stringResource(SharedRes.strings.proceed))
            }
        }
    }
}