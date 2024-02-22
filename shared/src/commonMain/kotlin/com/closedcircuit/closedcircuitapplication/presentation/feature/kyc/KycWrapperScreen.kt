package com.closedcircuit.closedcircuitapplication.presentation.feature.kyc

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.closedcircuit.closedcircuitapplication.domain.user.UserRepository
import com.closedcircuit.closedcircuitapplication.presentation.component.BaseScaffold
import com.closedcircuit.closedcircuitapplication.presentation.component.DefaultAppBar
import com.closedcircuit.closedcircuitapplication.presentation.navigation.transition.ScreenBasedTransition
import com.closedcircuit.closedcircuitapplication.presentation.theme.defaultHorizontalScreenPadding
import com.closedcircuit.closedcircuitapplication.presentation.theme.defaultVerticalScreenPadding
import com.closedcircuit.closedcircuitapplication.resources.SharedRes
import com.closedcircuit.closedcircuitapplication.util.observerWithScreen
import dev.icerock.moko.resources.compose.stringResource
import kotlinx.coroutines.flow.receiveAsFlow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


internal object KycWrapperScreen : Screen, KoinComponent {
    private val userRepository: UserRepository by inject()

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel = rememberScreenModel { KycViewModel(userRepository) }
        var showSuccessDialog by remember { mutableStateOf(false) }
        var showFailureDialog by remember { mutableStateOf(false) }

        viewModel.kycResultChannel.receiveAsFlow().observerWithScreen {
            when (it) {
                is KycResult.Failure -> showFailureDialog = true
                KycResult.Success -> showSuccessDialog = true
            }
        }

        viewModel.state?.let {
            ScreenContent(uiState = it, goBack = navigator::pop)
        }
    }
}

@Composable
private fun ScreenContent(uiState: KycUiState, goBack: () -> Unit) {
    var innerNavigator: Navigator? by remember { mutableStateOf(null) }
    val startScreen = remember {
        if (uiState.hasAttemptedKyc) KycStatusScreen()
        else KycHomeScreen()
    }

    val pop: () -> Unit = {
        if (innerNavigator?.canPop == true) innerNavigator?.pop()
        else goBack()
    }

    BaseScaffold(
        topBar = {
            DefaultAppBar(
                title = stringResource(SharedRes.strings.document_verification),
                mainAction = pop
            )
        },
        contentWindowInsets = WindowInsets.safeDrawing
    ) { innerPadding ->
        Column {
            Navigator(startScreen) {
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