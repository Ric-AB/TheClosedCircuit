package com.closedcircuit.closedcircuitapplication.sponsor.presentation.feature.fundedplan.approval

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.closedcircuit.closedcircuitapplication.common.domain.model.ID
import com.closedcircuit.closedcircuitapplication.common.presentation.component.AppExtendedFabWithLoader
import com.closedcircuit.closedcircuitapplication.common.presentation.component.BackgroundLoader
import com.closedcircuit.closedcircuitapplication.common.presentation.component.BaseScaffold
import com.closedcircuit.closedcircuitapplication.common.presentation.component.BodyText
import com.closedcircuit.closedcircuitapplication.common.presentation.component.DefaultAppBar
import com.closedcircuit.closedcircuitapplication.common.presentation.component.DefaultButton
import com.closedcircuit.closedcircuitapplication.common.presentation.component.EmptyScreen
import com.closedcircuit.closedcircuitapplication.common.presentation.component.MessageBarState
import com.closedcircuit.closedcircuitapplication.common.presentation.component.ZoomableImage
import com.closedcircuit.closedcircuitapplication.common.presentation.component.rememberMessageBarState
import com.closedcircuit.closedcircuitapplication.common.presentation.feature.chat.conversation.ConversationScreen
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.horizontalScreenPadding
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.primary6
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.verticalScreenPadding
import com.closedcircuit.closedcircuitapplication.common.util.observeWithScreen
import com.closedcircuit.closedcircuitapplication.common.util.orFalse
import com.closedcircuit.closedcircuitapplication.resources.SharedRes
import com.closedcircuit.closedcircuitapplication.sponsor.presentation.component.PlanImage
import dev.icerock.moko.resources.compose.painterResource
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.coroutines.flow.receiveAsFlow
import org.koin.core.component.KoinComponent
import org.koin.core.parameter.parametersOf


internal class StepApprovalScreen(
    private val planID: ID,
    private val stepID: ID,
    private val canApprove: Boolean,
    private val isStepApprovedByUser: Boolean
) : Screen, KoinComponent {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = getScreenModel<StepApprovalViewModel> {
            parametersOf(
                planID,
                stepID,
                canApprove,
                isStepApprovedByUser
            )
        }
        val messageBarState = rememberMessageBarState()

        viewModel.resultChannel.receiveAsFlow().observeWithScreen {
            when (it) {
                StepApprovalResult.ApproveBudgetSuccess ->
                    messageBarState.addSuccess("You successfully approved a budget item.")

                StepApprovalResult.ApproveStepSuccess ->
                    messageBarState.addSuccess("You successfully approved this step.") {
                        navigator.pop()
                    }

                is StepApprovalResult.ChatUserSuccess ->
                    navigator.push(ConversationScreen(it.chatUser))

                is StepApprovalResult.Error -> messageBarState.addError(it.message)
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
        state: StepApprovalUiState,
        messageBarState: MessageBarState,
        goBack: () -> Unit,
        onEvent: (StepApprovalUiEvent) -> Unit
    ) {
        BaseScaffold(
            messageBarState = messageBarState,
            showLoadingDialog = state.postLoading,
            topBar = {
                DefaultAppBar(
                    mainAction = goBack,
                    title = stringResource(SharedRes.strings.step_proofs_label)
                )
            },
            floatingActionButton = {
                StartChatFab(
                    showLoader = (state as? StepApprovalUiState.Content)?.loadingUser.orFalse(),
                    onClick = { onEvent(StepApprovalUiEvent.FetchChatUser) }
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier.fillMaxSize().padding(innerPadding)
            ) {
                when (state) {
                    is StepApprovalUiState.Content -> Body(state, onEvent)
                    is StepApprovalUiState.Error ->
                        EmptyScreen(
                            title = stringResource(SharedRes.strings.oops_label),
                            message = state.message
                        )

                    StepApprovalUiState.Loading -> BackgroundLoader()
                }
            }
        }
    }

    @Composable
    private fun Body(state: StepApprovalUiState.Content, onEvent: (StepApprovalUiEvent) -> Unit) {
        var selectedImage by remember { mutableStateOf<String?>(null) }

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(40.dp),
            contentPadding = PaddingValues(bottom = verticalScreenPadding)
        ) {
            items(state.proofItems) {
                ProofItem(
                    modifier = Modifier.fillMaxWidth(),
                    item = it,
                    canApproveBudget = state.canApproveBudget,
                    approveBudget = { onEvent(StepApprovalUiEvent.ApproveBudget(it.id)) },
                    onImageSelect = { url -> selectedImage = url }
                )
            }

            if (state.canApproveStep) {
                item {
                    DefaultButton(
                        enabled = state.stepApprovalEnabled,
                        onClick = { onEvent(StepApprovalUiEvent.ApproveStep) },
                        modifier = Modifier.fillMaxWidth()
                            .padding(horizontal = horizontalScreenPadding)
                    ) {
                        Text(stringResource(SharedRes.strings.approve_step_label))
                    }
                }
            }
        }

        AnimatedVisibility(visible = selectedImage != null) {
            val closeImage: () -> Unit = { selectedImage = null }

            Dialog(onDismissRequest = closeImage) {
                if (selectedImage != null) {
                    ZoomableImage(
                        image = selectedImage!!,
                        onCloseClick = closeImage,
                    )
                }
            }
        }
    }

    @Composable
    private fun ProofItem(
        modifier: Modifier,
        item: ProofItem,
        canApproveBudget: Boolean,
        approveBudget: () -> Unit,
        onImageSelect: (String) -> Unit
    ) {
        Column(modifier) {
            val commonModifier = remember { Modifier.padding(horizontal = horizontalScreenPadding) }
            HeaderText(
                modifier = commonModifier,
                text = stringResource(SharedRes.strings.budget_label)
            )

            Spacer(Modifier.height(4.dp))
            BodyText(modifier = commonModifier, text = item.name)

            Spacer(Modifier.height(20.dp))
            HeaderText(
                modifier = commonModifier,
                text = stringResource(SharedRes.strings.image_description_label)
            )

            Spacer(Modifier.height(4.dp))
            BodyText(modifier = commonModifier, text = item.description)

            Spacer(Modifier.height(20.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = horizontalScreenPadding)
            ) {
                item.images.forEach {
                    PlanImage(
                        modifier = Modifier.size(200.dp, 152.dp).clickable { onImageSelect(it) },
                        imageUrl = it,
                    )
                }
            }

            if (canApproveBudget) {
                Spacer(Modifier.height(20.dp))
                ApproveButton(
                    modifier = commonModifier.align(Alignment.End),
                    isApproved = item.isApproved,
                    onClick = approveBudget
                )
            }
        }
    }

    @Composable
    private fun ApproveButton(modifier: Modifier, isApproved: Boolean, onClick: () -> Unit) {
        AnimatedContent(targetState = isApproved, modifier = modifier) { isApprovedState ->
            if (isApprovedState) {
                Button(
                    colors = ButtonDefaults.buttonColors(containerColor = primary6),
                    contentPadding = PaddingValues(horizontal = 32.dp),
                    onClick = { },
                    shape = Shapes().small,
                ) {
                    Text(
                        text = stringResource(SharedRes.strings.approved_label),
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(Modifier.width(8.dp))
                    Image(
                        painter = painterResource(SharedRes.images.ic_green_check),
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                }
            } else {
                Button(
                    contentPadding = PaddingValues(horizontal = 32.dp),
                    onClick = onClick,
                    shape = Shapes().small,
                ) {
                    Text(stringResource(SharedRes.strings.approve_label))
                }
            }
        }
    }

    @Composable
    private fun HeaderText(modifier: Modifier, text: String) {
        Text(
            modifier = modifier,
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )
    }

    @Composable
    private fun StartChatFab(showLoader: Boolean, onClick: () -> Unit) {
        AppExtendedFabWithLoader(
            onClick = onClick,
            text = { Text(text = stringResource(SharedRes.strings.start_chat_label)) },
            icon = {
                if (showLoader) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeCap = StrokeCap.Round,
                        strokeWidth = 3.dp
                    )
                } else {
                    Icon(
                        painter = painterResource(SharedRes.images.ic_square_pen),
                        contentDescription = null
                    )
                }
            },
            showLoader = showLoader
        )
    }
}