package com.closedcircuit.closedcircuitapplication.presentation.feature.planmanagement.createplan

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.closedcircuit.closedcircuitapplication.domain.usecase.CreatePlanUseCase
import com.closedcircuit.closedcircuitapplication.presentation.component.BaseScaffold
import com.closedcircuit.closedcircuitapplication.presentation.component.DefaultAppBar
import com.closedcircuit.closedcircuitapplication.presentation.component.MessageBarState
import com.closedcircuit.closedcircuitapplication.presentation.component.SuccessScreen
import com.closedcircuit.closedcircuitapplication.presentation.component.rememberMessageBarState
import com.closedcircuit.closedcircuitapplication.presentation.feature.planmanagement.planlist.PlanListScreen
import com.closedcircuit.closedcircuitapplication.presentation.navigation.transition.ScreenBasedTransition
import com.closedcircuit.closedcircuitapplication.presentation.theme.defaultHorizontalScreenPadding
import com.closedcircuit.closedcircuitapplication.presentation.theme.defaultVerticalScreenPadding
import com.closedcircuit.closedcircuitapplication.resources.SharedRes
import com.closedcircuit.closedcircuitapplication.util.observerWithScreen
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.coroutines.flow.receiveAsFlow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

internal object CreatePlanWrapperScreen : Screen, KoinComponent {
    private val createPlanUseCase: CreatePlanUseCase by inject()

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = rememberScreenModel { CreatePlanViewModel(createPlanUseCase) }
        val messageBarState = rememberMessageBarState()

        viewModel.createPlanResultChannel.receiveAsFlow().observerWithScreen {
            when (it) {
                is CreatePlanResult.Failure -> messageBarState.addError(it.message)
                CreatePlanResult.Success -> {
                    navigator.push(
                        SuccessScreen(
                            title = "",
                            message = "",
                            primaryAction = { navigator.popUntil { screen -> screen is PlanListScreen } }
                        )
                    )
                }
            }
        }

        ScreenContent(
            messageBarState = messageBarState,
            uiState = viewModel.state,
            goBack = navigator::pop
        )
    }
}

@Composable
private fun ScreenContent(
    messageBarState: MessageBarState,
    uiState: CreatePlanUIState,
    goBack: () -> Unit
) {
    var innerNavigator: Navigator? by remember { mutableStateOf(null) }

    BaseScaffold(
        messageBarState = messageBarState,
        isLoading = uiState.isLoading,
        topBar = {
            CreatePlanAppBar(
                progress = innerNavigator?.size?.div(2F) ?: 0.5F,
                onNavIconClick = {
                    if (innerNavigator?.canPop == true) innerNavigator?.pop()
                    else goBack()
                }
            )
        },
        contentWindowInsets = WindowInsets.safeDrawing
    ) { innerPadding ->
        Column {
            Navigator(PlanClassificationScreen()) {
                innerNavigator = it
                ScreenBasedTransition(
                    navigator = it,
                    modifier = Modifier.fillMaxSize()
                        .padding(innerPadding)
                        .verticalScroll(rememberScrollState())
                        .padding(
                            horizontal = defaultHorizontalScreenPadding,
                            vertical = defaultVerticalScreenPadding
                        )
                )
            }
        }
    }
}

@Composable
private fun CreatePlanAppBar(progress: Float, onNavIconClick: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        val animatedProgress by animateFloatAsState(
            targetValue = progress,
            animationSpec = tween(700)
        )

        DefaultAppBar(
            title = stringResource(SharedRes.strings.create_plan),
            mainAction = onNavIconClick
        )

        LinearProgressIndicator(
            modifier = Modifier.fillMaxWidth(),
            progress = animatedProgress,
            strokeCap = StrokeCap.Round
        )
    }
}