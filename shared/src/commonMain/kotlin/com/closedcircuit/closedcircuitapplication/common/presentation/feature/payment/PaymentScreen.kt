package com.closedcircuit.closedcircuitapplication.common.presentation.feature.payment

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.core.screen.ScreenKey
import cafe.adriel.voyager.koin.getNavigatorScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.closedcircuit.closedcircuitapplication.common.presentation.component.BaseScaffold
import com.closedcircuit.closedcircuitapplication.common.presentation.navigation.navigationResult
import com.closedcircuit.closedcircuitapplication.common.util.observeWithScreen
import com.multiplatform.webview.web.WebView
import com.multiplatform.webview.web.WebViewState
import com.multiplatform.webview.web.rememberWebViewState
import kotlinx.coroutines.flow.receiveAsFlow
import org.koin.core.component.KoinComponent


internal class PaymentScreen(private val url: String) : Screen, KoinComponent {

    override val key: ScreenKey
        get() = PAYMENT_RESULT

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val navigationResult = navigator.navigationResult
        val webViewState = rememberWebViewState(url)
        val viewModel = navigator.getNavigatorScreenModel<PaymentViewModel>()

        viewModel.resultChannel.receiveAsFlow().observeWithScreen {
            when (it) {
                is PaymentResult.Error -> {}
                PaymentResult.Success -> navigationResult.popWithResult(true)
            }
        }

        LaunchedEffect(webViewState.lastLoadedUrl) {
            viewModel.onEvent(PaymentUiEvent.UrlChange(webViewState.lastLoadedUrl.orEmpty()))
        }

        ScreenContent(webViewState = webViewState)
    }

    @Composable
    private fun ScreenContent(webViewState: WebViewState) {
        BaseScaffold { innerPadding ->
            Column(
                modifier = Modifier.fillMaxSize()
                    .padding(innerPadding)
            ) {
                WebView(state = webViewState, modifier = Modifier.fillMaxSize())
            }
        }
    }

    companion object {
        const val PAYMENT_RESULT = "payment_result"
    }
}