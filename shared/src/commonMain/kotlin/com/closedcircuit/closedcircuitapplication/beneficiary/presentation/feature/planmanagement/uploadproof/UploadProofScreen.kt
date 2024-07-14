package com.closedcircuit.closedcircuitapplication.beneficiary.presentation.feature.planmanagement.uploadproof

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.closedcircuit.closedcircuitapplication.common.domain.model.ID
import com.closedcircuit.closedcircuitapplication.common.presentation.LocalImagePicker
import com.closedcircuit.closedcircuitapplication.common.presentation.component.BaseScaffold
import com.closedcircuit.closedcircuitapplication.common.presentation.component.BodyText
import com.closedcircuit.closedcircuitapplication.common.presentation.component.DefaultAppBar
import com.closedcircuit.closedcircuitapplication.common.presentation.component.DefaultButton
import com.closedcircuit.closedcircuitapplication.common.presentation.component.DefaultOutlinedTextField
import com.closedcircuit.closedcircuitapplication.common.presentation.component.TitleText
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.horizontalScreenPadding
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.verticalScreenPadding
import com.closedcircuit.closedcircuitapplication.common.presentation.util.InputField
import com.closedcircuit.closedcircuitapplication.resources.SharedRes
import com.skydoves.landscapist.coil3.CoilImage
import dev.icerock.moko.resources.compose.painterResource
import dev.icerock.moko.resources.compose.stringResource
import org.koin.core.component.KoinComponent
import org.koin.core.parameter.parametersOf


internal class UploadProofScreen(private val budgetID: ID) : Screen, KoinComponent {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getScreenModel<UploadProofViewModel> { parametersOf(budgetID) }

        ScreenContent(
            state = viewModel.state.value,
            goBack = navigator::pop,
            onEvent = viewModel::onEvent
        )
    }

    @Composable
    private fun ScreenContent(
        state: UploadProofUiState,
        goBack: () -> Unit,
        onEvent: (UploadProofUiEvent) -> Unit
    ) {
        BaseScaffold(
            topBar = { DefaultAppBar(mainAction = goBack) }
        ) { innerPadding ->
            Column(
                modifier = Modifier.fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = horizontalScreenPadding, vertical = verticalScreenPadding)
            ) {
                val imagePicker = LocalImagePicker.current
                imagePicker.registerPicker {
                    onEvent(UploadProofUiEvent.ImageAdded(it))
                }

                TitleText(stringResource(SharedRes.strings.budget_item_proof_label))

                Spacer(Modifier.height(4.dp))
                BodyText(stringResource(SharedRes.strings.budget_item_proof_message_label))

                Spacer(Modifier.height(40.dp))
                UploadComponent(
                    icon = painterResource(SharedRes.images.ic_photos_stack),
                    text = stringResource(SharedRes.strings.select_image_label),
                    onClick = { imagePicker.pickImage() }
                )

                Spacer(Modifier.height(8.dp))
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(state.uploadItems) {
                        CoilImage(
                            imageModel = { it.bytes },
                            modifier = Modifier.size(130.dp, 75.dp).clip(Shapes().small)
                        )
                    }
                }

                Spacer(Modifier.height(20.dp))
                DefaultOutlinedTextField(
                    inputField = state.titleField,
                    onValueChange = {},
                    label = stringResource(SharedRes.strings.proof_title_label),
                    enabled = false
                )

                Spacer(Modifier.height(20.dp))
                DefaultOutlinedTextField(
                    inputField = InputField(),
                    onValueChange = { onEvent(UploadProofUiEvent.ProofDescriptionChange(it)) },
                    label = stringResource(SharedRes.strings.proof_description_label)
                )

                Spacer(Modifier.height(40.dp))
                DefaultButton(onClick = {}, enabled = state.canUpload) {
                    Text(stringResource(SharedRes.strings.upload_label))
                }
            }
        }
    }

    @Composable
    private fun UploadComponent(icon: Painter, text: String, onClick: () -> Unit) {
        val colorSurface = MaterialTheme.colorScheme.surfaceVariant
        OutlinedCard(
            modifier = Modifier.fillMaxWidth(),
            shape = Shapes().medium,
            border = BorderStroke(1.dp, colorSurface),
            onClick = onClick
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
                    .padding(end = 12.dp),
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size(72.dp)
                        .background(colorSurface)
                ) {
                    Icon(
                        painter = icon,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(Modifier.width(16.dp))
                Text(text = text, color = Color.Gray)

                Spacer(Modifier.weight(1f))
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}