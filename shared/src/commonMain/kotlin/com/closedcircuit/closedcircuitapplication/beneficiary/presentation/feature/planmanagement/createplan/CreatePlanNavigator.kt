package com.closedcircuit.closedcircuitapplication.beneficiary.presentation.feature.planmanagement.createplan

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.closedcircuit.closedcircuitapplication.beneficiary.presentation.feature.planmanagement.planlist.PlanListScreen
import com.closedcircuit.closedcircuitapplication.beneficiary.presentation.navigation.transition.ScreenBasedTransition
import com.closedcircuit.closedcircuitapplication.common.presentation.component.BaseScaffold
import com.closedcircuit.closedcircuitapplication.common.presentation.component.DefaultAppBar
import com.closedcircuit.closedcircuitapplication.common.presentation.component.MessageBarState
import com.closedcircuit.closedcircuitapplication.common.presentation.component.SuccessScreen
import com.closedcircuit.closedcircuitapplication.common.presentation.component.rememberMessageBarState
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.horizontalScreenPadding
import com.closedcircuit.closedcircuitapplication.common.presentation.theme.verticalScreenPadding
import com.closedcircuit.closedcircuitapplication.common.util.observeWithScreen
import com.closedcircuit.closedcircuitapplication.common.util.orFalse
import com.closedcircuit.closedcircuitapplication.resources.SharedRes
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.coroutines.flow.receiveAsFlow
import org.koin.core.component.KoinComponent

internal object CreatePlanNavigator : Screen, KoinComponent {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        var innerNavigator: Navigator? by remember { mutableStateOf(null) }
        val viewModel = innerNavigator?.getNavigatorScreenModel<CreatePlanViewModel>()
        val messageBarState = rememberMessageBarState()

        viewModel?.createPlanResultChannel?.receiveAsFlow()?.observeWithScreen {
            when (it) {
                is CreatePlanResult.Failure -> messageBarState.addError(it.message)
                CreatePlanResult.Success -> {
                    navigator.push(
                        SuccessScreen(
                            title = "Plan created successfully.",
                            message = "Now that you've created your plan, complete the details of your action steps and budget required to bring your plan idea to life. See info on what steps and budgets are.",
                            primaryAction = { navigator.popUntil { screen -> screen is PlanListScreen } }
                        )
                    )
                }
            }
        }

        ScreenContent(
            messageBarState = messageBarState,
            uiState = viewModel?.state,
            innerNavigator = innerNavigator,
            onLaunch = { innerNavigator = it },
            goBack = {
                if (innerNavigator?.canPop == true) innerNavigator?.pop()
                else navigator.pop()
            }
        )
    }
}

@Composable
private fun ScreenContent(
    messageBarState: MessageBarState,
    uiState: CreatePlanUIState?,
    innerNavigator: Navigator?,
    onLaunch: (Navigator) -> Unit,
    goBack: () -> Unit
) {

    BaseScaffold(
        messageBarState = messageBarState,
        showLoadingDialog = uiState?.isLoading.orFalse(),
        topBar = {
            CreatePlanAppBar(
                progress = innerNavigator?.size?.div(2F) ?: 0.5F,
                onNavIconClick = goBack
            )
        },
        contentWindowInsets = WindowInsets.safeDrawing
    ) { innerPadding ->
        Column {
            Navigator(PlanClassificationScreen()) {
                LaunchedEffect(it) {
                    onLaunch(it)
                }

                ScreenBasedTransition(
                    navigator = it,
                    modifier = Modifier.fillMaxSize()
                        .padding(innerPadding)
                        .verticalScroll(rememberScrollState())
                        .padding(
                            horizontal = horizontalScreenPadding,
                            vertical = verticalScreenPadding
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
            progress = { animatedProgress },
            modifier = Modifier.fillMaxWidth(),
            strokeCap = StrokeCap.Round,
        )
    }
}