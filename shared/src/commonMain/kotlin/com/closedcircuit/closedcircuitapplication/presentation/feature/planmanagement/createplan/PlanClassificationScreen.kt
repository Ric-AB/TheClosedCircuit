package com.closedcircuit.closedcircuitapplication.presentation.feature.planmanagement.createplan

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.closedcircuit.closedcircuitapplication.domain.usecase.CreatePlanUseCase
import com.closedcircuit.closedcircuitapplication.presentation.LocalImagePicker
import com.closedcircuit.closedcircuitapplication.presentation.component.DefaultButton
import com.closedcircuit.closedcircuitapplication.presentation.component.EditableAvatar
import com.closedcircuit.closedcircuitapplication.presentation.component.LargeDropdownMenu
import com.closedcircuit.closedcircuitapplication.resources.SharedRes
import dev.icerock.moko.resources.compose.stringResource
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

internal class PlanClassificationScreen : Screen, KoinComponent {
    private val createPlanUseCase: CreatePlanUseCase by inject()

    @Composable
    override fun Content() {
        val viewModel =
            CreatePlanNavigator.rememberScreenModel { CreatePlanViewModel(createPlanUseCase) }

        val navigator = LocalNavigator.currentOrThrow
        ScreenContent(
            uiState = viewModel.state,
            onEvent = viewModel::onEvent,
            navigateToPlanInfoScreen = { navigator.push(PlanInfoScreen()) }
        )
    }
}

@Composable
private fun ScreenContent(
    uiState: CreatePlanUIState,
    onEvent: (CreatePlanUiEvent) -> Unit,
    navigateToPlanInfoScreen: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        val imagePicker = LocalImagePicker.current
        imagePicker.registerPicker {

        }

        Column(modifier = Modifier.fillMaxWidth().weight(1f)) {
            EditableAvatar(
                bytes = null,
                size = DpSize(70.dp, 70.dp),
                onClick = { imagePicker.pickImage() }
            )

            Spacer(modifier = Modifier.height(24.dp))
            LargeDropdownMenu(
                modifier = Modifier.fillMaxWidth(),
                label = stringResource(SharedRes.strings.select_category),
                items = uiState.categories,
                selectedItemToString = { it.value },
                selectedItem = uiState.category,
                onItemSelected = { _, item -> onEvent(CreatePlanUiEvent.CategoryChange(item)) },
            )

            Spacer(modifier = Modifier.height(24.dp))
            LargeDropdownMenu(
                modifier = Modifier.fillMaxWidth(),
                label = stringResource(SharedRes.strings.select_sector),
                items = uiState.sectors,
                selectedItemToString = { it.value },
                selectedItem = uiState.sector,
                onItemSelected = { _, item -> onEvent(CreatePlanUiEvent.SectorChange(item)) },
            )

            Spacer(modifier = Modifier.height(24.dp))
            LargeDropdownMenu(
                modifier = Modifier.fillMaxWidth(),
                label = stringResource(SharedRes.strings.select_business_type),
                items = uiState.businessTypes,
                selectedItemToString = { it.value },
                selectedItem = uiState.businessType,
                onItemSelected = { _, item -> onEvent(CreatePlanUiEvent.BusinessType(item)) },
            )
        }

        val isEnabled = when (uiState.category?.id) {
            "project" -> true
            else -> uiState.category != null && uiState.businessType != null && uiState.sector != null
        }
        DefaultButton(onClick = navigateToPlanInfoScreen, enabled = isEnabled) {
            Text(text = stringResource(SharedRes.strings.next))
        }
    }
}