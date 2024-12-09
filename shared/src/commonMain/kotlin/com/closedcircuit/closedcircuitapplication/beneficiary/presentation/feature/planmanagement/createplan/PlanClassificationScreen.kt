package com.closedcircuit.closedcircuitapplication.beneficiary.presentation.feature.planmanagement.createplan

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.closedcircuit.closedcircuitapplication.common.presentation.LocalImagePicker
import com.closedcircuit.closedcircuitapplication.common.presentation.component.DefaultButton
import com.closedcircuit.closedcircuitapplication.common.presentation.component.EditableAvatar
import com.closedcircuit.closedcircuitapplication.common.presentation.component.TextFieldDialogMenu
import com.closedcircuit.closedcircuitapplication.common.presentation.navigation.screentransition.CustomScreenTransition
import com.closedcircuit.closedcircuitapplication.common.presentation.navigation.screentransition.SlideOverTransition
import com.closedcircuit.closedcircuitapplication.resources.SharedRes
import dev.icerock.moko.resources.compose.stringResource
import org.koin.core.component.KoinComponent

internal class PlanClassificationScreen : Screen, KoinComponent, CustomScreenTransition by SlideOverTransition {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = navigator.getNavigatorScreenModel<CreatePlanViewModel>()
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
        imagePicker.registerPicker { onEvent(CreatePlanUiEvent.PlanImageChange(it)) }

        Column(modifier = Modifier.fillMaxWidth().weight(1f)) {
            EditableAvatar(
                bytes = uiState.planImageBytes,
                size = DpSize(70.dp, 70.dp),
                onClick = { imagePicker.pickImage() },
                shape = MaterialTheme.shapes.medium
            )

            Spacer(modifier = Modifier.height(24.dp))
            TextFieldDialogMenu(
                modifier = Modifier.fillMaxWidth(),
                label = stringResource(SharedRes.strings.select_category),
                items = uiState.categories,
                itemToString = { it.value },
                selectedItem = uiState.category,
                onItemSelected = { _, item -> onEvent(CreatePlanUiEvent.CategoryChange(item)) },
            )

            Spacer(modifier = Modifier.height(24.dp))
            TextFieldDialogMenu(
                modifier = Modifier.fillMaxWidth(),
                label = stringResource(SharedRes.strings.select_sector),
                items = uiState.sectors,
                itemToString = { it.value },
                selectedItem = uiState.sector,
                onItemSelected = { _, item -> onEvent(CreatePlanUiEvent.SectorChange(item)) },
            )

            Spacer(modifier = Modifier.height(24.dp))
            TextFieldDialogMenu(
                modifier = Modifier.fillMaxWidth(),
                label = stringResource(SharedRes.strings.select_business_type),
                items = uiState.businessTypes,
                itemToString = { it.value },
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