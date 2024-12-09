package com.closedcircuit.closedcircuitapplication.beneficiary.presentation.feature.planmanagement.uploadproof

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.closedcircuit.closedcircuitapplication.common.presentation.component.CircularIndicator
import com.closedcircuit.closedcircuitapplication.common.presentation.component.DefaultAppBar
import com.closedcircuit.closedcircuitapplication.common.presentation.component.DefaultButton
import com.closedcircuit.closedcircuitapplication.common.presentation.component.DefaultOutlinedTextField
import com.closedcircuit.closedcircuitapplication.common.presentation.component.MessageBarState
import com.closedcircuit.closedcircuitapplication.common.presentation.component.TitleText
import com.closedcircuit.closedcircuitapplication.common.presentation.component.rememberMessageBarState
import com.closedcircuit.closedcircuitapplication.common.presentation.navigation.screentransition.CustomScreenTransition
import com.closedcircuit.closedcircuitapplication.common.presentation.navigation.screentransition.SlideOverTransition
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.horizontalScreenPadding
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.verticalScreenPadding
import com.closedcircuit.closedcircuitapplication.common.util.observeWithScreen
import com.closedcircuit.closedcircuitapplication.resources.SharedRes
import com.skydoves.landscapist.coil3.CoilImage
import dev.icerock.moko.resources.compose.painterResource
import dev.icerock.moko.resources.compose.stringResource
import org.koin.core.component.KoinComponent
import org.koin.core.parameter.parametersOf


internal class UploadProofScreen(private val budgetID: ID) : Screen, KoinComponent,
    CustomScreenTransition by SlideOverTransition {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getScreenModel<UploadProofViewModel> { parametersOf(budgetID) }
        val messageBarState = rememberMessageBarState()

        viewModel.resultChannel.observeWithScreen {
            when (it) {
                is UploadProofResult.UploadError -> messageBarState.addError(it.message)
                UploadProofResult.UploadSuccess ->
                    messageBarState.addSuccess(message = "Uploaded successfully") {
                        navigator.pop()
                    }
            }
        }

        ScreenContent(
            state = viewModel.state.value,
            messageBarState = messageBarState,
            goBack = navigator::pop,
            onEvent = viewModel::onEvent
        )
    }

    @Composable
    private fun ScreenContent(
        state: UploadProofUiState,
        messageBarState: MessageBarState,
        goBack: () -> Unit,
        onEvent: (UploadProofUiEvent) -> Unit
    ) {
        BaseScaffold(
            topBar = { DefaultAppBar(mainAction = goBack) },
            contentWindowInsets = WindowInsets.safeDrawing,
            messageBarState = messageBarState,
            showLoadingDialog = state.isLoading
        ) { innerPadding ->
            Column(
                modifier = Modifier.fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
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
                    enabled = state.canEditUpload,
                    onClick = { imagePicker.pickImage() }
                )

                if (state.isLoadingUploads) {
                    Spacer(Modifier.height(12.dp))
                    CircularIndicator(size = 18.dp)
                }

                UploadRow(
                    items = state.uploadItems,
                    retrieveData = { it.bytes },
                    removeImage = { onEvent(UploadProofUiEvent.ImageRemoved(it)) }
                )

                UploadRow(items = state.existingUploadedItems, retrieveData = { it.url })
                Spacer(Modifier.height(20.dp))
                DefaultOutlinedTextField(
                    inputField = state.titleField,
                    onValueChange = {},
                    label = stringResource(SharedRes.strings.proof_title_label),
                    enabled = false
                )

                Spacer(Modifier.height(20.dp))
                DefaultOutlinedTextField(
                    inputField = state.descriptionField,
                    onValueChange = { onEvent(UploadProofUiEvent.ProofDescriptionChange(it)) },
                    label = stringResource(SharedRes.strings.proof_description_label)
                )

                Spacer(Modifier.height(40.dp))
                DefaultButton(
                    onClick = { onEvent(UploadProofUiEvent.Submit) },
                    enabled = state.canSubmitUpload
                ) {
                    Text(stringResource(SharedRes.strings.upload_label))
                }
            }
        }
    }

    @Composable
    private fun <T> UploadRow(
        items: List<T>,
        retrieveData: (T) -> Any,
        removeImage: ((Int) -> Unit)? = null
    ) {
        if (items.isNotEmpty()) {
            Column {
                Spacer(Modifier.height(12.dp))
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    itemsIndexed(items) { index, item ->
                        val action: () -> Unit = { removeImage?.invoke(index) }
                        UploadImage(
                            data = retrieveData(item),
                            onRemoveIconClick = if (removeImage != null) action else null
                        )
                    }
                }
            }
        }
    }

    @Composable
    private fun UploadImage(data: Any, onRemoveIconClick: (() -> Unit)? = null) {
        Box(Modifier.size(130.dp, 75.dp)) {
            CoilImage(
                imageModel = { data },
                modifier = Modifier.fillMaxSize().clip(Shapes().small)
            )

            onRemoveIconClick?.let { action ->
                IconButton(
                    onClick = action,
                    modifier = Modifier.padding(top = 4.dp, end = 4.dp)
                        .clip(CircleShape)
                        .background(Color.Gray.copy(alpha = 0.7f))
                        .size(24.dp)
                        .align(Alignment.TopEnd)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }

    }

    @Composable
    private fun UploadComponent(
        icon: Painter,
        text: String,
        enabled: Boolean,
        onClick: () -> Unit
    ) {
        val colorSurface = MaterialTheme.colorScheme.surfaceVariant
        OutlinedCard(
            modifier = Modifier.fillMaxWidth(),
            shape = Shapes().medium,
            border = BorderStroke(1.dp, colorSurface),
            onClick = onClick,
            enabled = enabled
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